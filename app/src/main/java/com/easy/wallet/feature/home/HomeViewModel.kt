package com.easy.wallet.feature.home

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.easy.framework.base.BaseViewModel
import com.easy.framework.model.ResultStatus
import com.easy.wallet.data.*
import com.easy.wallet.ext.downDecimal
import com.easy.wallet.feature.home.uimodel.AssetListUIEvent
import io.uniflow.android.AndroidDataFlow
import io.uniflow.core.flow.actionOn
import io.uniflow.core.flow.data.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import org.koin.core.component.KoinComponent
import timber.log.Timber
import java.math.BigDecimal

class HomeViewModel(
    private val state: SavedStateHandle
) : AndroidDataFlow(), KoinComponent {

    companion object {
        private const val SAVE_KEY_BALANCE = "balance"
    }

    private val _assets = mutableListOf<Asset>()

    private val bChannel = Channel<Asset>()

    private suspend fun loadBalances() {
        setState { UIState.Loading }
        WalletDataSDK.activeAssets()
            .map {
                Asset(
                    coinInfo = CurrencyInfo.mapping(it),
                    provider = WalletDataSDK.injectProvider(
                        it.coin_slug,
                        it.coin_symbol,
                        it.coin_decimal
                    )
                )
            }.let {
                _assets.apply {
                    clear()
                    addAll(it)
                }
                setState { AssetListState(_assets) }
                it.forEach { item ->
                    item.provider.getBalance(item.provider.getAddress(false)).map {
                        bChannel.send(
                            item.copy(
                                balance = it.toBigDecimal().downDecimal(item.coinInfo.decimal)
                            )
                        )
                    }.catch {
                        bChannel.send(item.copy(balance = BigDecimal.ZERO))
                    }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
                }
                state.set(SAVE_KEY_BALANCE, true)
                it
            }

        viewModelScope.launch {
            while (isActive && !bChannel.isClosedForReceive) {
                select<Unit> {
                    bChannel.onReceiveCatching {
                        updateItem(it.getOrNull())
                        action {
                            setState { AssetListState(_assets) }
                        }
                    }
                }
            }
        }
    }

    fun refresh() = actionOn<AssetListState>(
        onAction = {
            sendEvent(AssetListUIEvent.RefreshEvent)
            loadBalances()
        }
    )

    fun initBalances() = action {
        val hasSaved = state.get<Boolean>(SAVE_KEY_BALANCE)
        if (hasSaved == null || hasSaved == false) loadBalances()
    }

    private fun updateItem(asset: Asset?) {
        asset?.also {
            _assets.indexOfFirst { asset.coinInfo == it.coinInfo }.let {
                if (it >= 0) _assets[it] = asset
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onCleared() {
        bChannel.close()
        super.onCleared()
    }
}
