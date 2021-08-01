package com.easy.wallet.feature.send

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.easy.wallet.data.CurrencyInfo
import com.easy.wallet.data.WalletDataSDK
import com.easy.wallet.data.data.model.SendModel
import com.easy.wallet.ext.byDownDecimal
import com.easy.wallet.feature.send.uimodel.SendStates
import com.easy.wallet.feature.send.uimodel.SendUIEvents
import com.easy.wallet.model.bean.SendModelWrap
import io.uniflow.android.AndroidDataFlow
import io.uniflow.core.coroutines.onFlow
import io.uniflow.core.flow.data.UIState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import timber.log.Timber

class SendViewModel(
    private val state: SavedStateHandle,
    private val currencyInfo: CurrencyInfo,
) : AndroidDataFlow(), KoinComponent {

    private val coinProvider =
        WalletDataSDK.injectProvider(currencyInfo.slug, currencyInfo.symbol, currencyInfo.decimal)

    private val enterModel = EnterModel()

    init {
        viewModelScope.launch {
            val flow = coinProvider.getBalance(coinProvider.getAddress(false))
            onFlow(
                flow = {
                    flow.onStart {
                        setState(UIState.Loading)
                    }
                },
                doAction = { value ->
                    val balance = value.toBigDecimal().byDownDecimal(
                        currencyInfo.decimal,
                        currencyInfo.displayDecimal
                    )
                    setState(SendStates.InfoState(currencyInfo, balance))
                },
                onError = { error, _ ->
                    Timber.e(error)
                    setState { SendStates.BalanceFailedState(currencyInfo) }
                }
            )
        }
    }

    fun feeUnit(): String {
        return when (currencyInfo.symbol) {
            "BTC", "BCH", "DOGE" -> "sat/byte"
            "ETH", "CRO", "BNB" -> "Gas Price(GWEI)"
            else -> currencyInfo.symbol
        }
    }

    fun updateEnter(fieldKey: ModelFieldKey, value: String) {
        when (fieldKey) {
            ModelFieldKey.ADDRESS -> {
                enterModel.toAddress = value
            }
            ModelFieldKey.AMOUNT -> {
                enterModel.amount = value
            }
            ModelFieldKey.MEMO -> {
                enterModel.memo = value
            }
        }
    }

    fun isFullEnter(): Boolean = enterModel.isFull()

    @ExperimentalCoroutinesApi
    fun buildTransaction(
        fee: Float,
        useMax: Boolean = false
    ) = action(
        onAction = {
            sendEvent(SendUIEvents.ToSend)
            val flow = coinProvider.buildTransactionPlan(
                enterModel.toSendModel(fee, useMax, currencyInfo.decimal)
            )
            onFlow(
                flow = { flow },
                doAction = {
                    if (it.rawData.isEmpty()) {
                        sendEvent(SendUIEvents.BuildError)
                    } else {
                        val model = SendModelWrap(
                            gasLimit = it.gasLimit,
                            gas = it.gas,
                            to = it.toAddress,
                            from = it.fromAddress,
                            amount = it.amount,
                            symbol = currencyInfo.symbol,
                            feeDecimals = it.feeDecimals,
                            memo = it.memo,
                            rawData = it.rawData
                        )
                        setState {
                            SendStates.BuildState(model)
                        }
                    }
                },
                onError = { error, _ ->
                    Timber.e(error)
                    sendEvent(SendUIEvents.BuildError)
                }
            )
        },
        onError = { _, _ ->
            sendEvent(SendUIEvents.BuildError)
        }
    )
}

enum class ModelFieldKey {
    AMOUNT, ADDRESS, MEMO
}

data class EnterModel(
    var amount: String = "",
    var toAddress: String = "",
    var memo: String = ""
) {
    fun isFull(): Boolean {
        return amount.isNotBlank() and toAddress.isNotBlank()
    }

    fun toSendModel(fee: Float, useMax: Boolean = false, decimal: Int): SendModel {
        return SendModel(
            amount = this.amount.toBigDecimal().scaleByPowerOfTen(decimal).toBigInteger(),
            to = this.toAddress,
            memo = this.memo,
            feeByte = fee,
            useMax = useMax
        )
    }
}
