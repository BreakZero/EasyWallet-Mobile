package com.easy.wallet.feature.send

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.easy.framework.base.BaseViewModel
import com.easy.framework.model.RequestState
import com.easy.wallet.data.CurrencyInfo
import com.easy.wallet.data.WalletDataSDK
import com.easy.wallet.data.data.model.SendModel
import com.easy.wallet.data.provider.IProvider
import com.easy.wallet.ext.byDownDecimal
import com.easy.wallet.model.bean.SendModelWrap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.koin.core.component.KoinComponent
import timber.log.Timber

class SendViewModel(
    private val state: SavedStateHandle,
    private val currencyInfo: CurrencyInfo,
) : BaseViewModel(), KoinComponent {
    companion object {
        private const val KEY_BALANCE = "key-balance"
    }

    private val coinProvider = WalletDataSDK.injectProvider(currencyInfo.slug, currencyInfo.symbol, currencyInfo.decimal)

    private val enterModel = EnterModel()

    fun initInfo() = liveData {
        emit(currencyInfo)
    }

    fun loadBalance() = liveData {
        if (state.get<Boolean>(INIT_STATE) == true) {
            emit(RequestState.Success(state.get<String>(KEY_BALANCE)))
            return@liveData
        }
        coinProvider.getBalance(coinProvider.getAddress(false))
            .onStart {
                emit(RequestState.Loading)
            }.catch {
                Timber.e(it)
                emit(RequestState.Error(error = it))
            }.collect {
                state.set(INIT_STATE, true)

                val balance = it.toBigDecimal().byDownDecimal(
                    currencyInfo.decimal,
                    currencyInfo.displayDecimal
                )
                state.set(KEY_BALANCE, "$balance ${currencyInfo.symbol}")
                emit(RequestState.Success("$balance ${currencyInfo.symbol}"))
            }
    }

    fun feeUnit(): String {
        return when (currencyInfo.symbol) {
            "BTC" -> "sat/byte"
            "ETH", "CRO" -> "Gas Price(GWEI)"
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
        useMax: Boolean = false,
        onNext: (SendModelWrap?) -> Unit
    ) {
        coinProvider.buildTransactionPlan(
            enterModel.toSendModel(fee, useMax, currencyInfo.decimal)
        ).onEach {
            onNext.invoke(
                if (it.rawData.isEmpty()) null
                else SendModelWrap(
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
            )
        }.catch {
            Timber.e(it)
            onNext.invoke(null)
        }.launchIn(viewModelScope)
    }
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
