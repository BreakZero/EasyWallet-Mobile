package com.easy.wallet.feature.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.easy.framework.base.BaseViewModel
import com.easy.framework.model.ResultStatus
import com.easy.wallet.data.Asset
import com.easy.wallet.data.CurrencyInfo
import com.easy.wallet.data.WalletDataSDK
import com.easy.wallet.ext.downDecimal
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
import java.math.BigDecimal

class HomeViewModel(
    private val state: SavedStateHandle
) : BaseViewModel(), KoinComponent {

    companion object {
        private const val SAVE_KEY_BALANCE = "balance"
    }

    private val _assets = mutableListOf<Asset>()

    val assets = MutableLiveData<List<Asset>>()

    private val bChannel = Channel<Asset>()

    fun loadBalances(isRefresh: Boolean) {
        if (isRefresh.not() and (state.get<Boolean>(SAVE_KEY_BALANCE) == true)) return
        WalletDataSDK.activeAssets()
            .map {
                Asset(
                    coinInfo = CurrencyInfo.mapping(it),
                    balance = ResultStatus.Loading,
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
                assets.postValue(_assets)
                it.forEach { item ->
                    item.provider.getBalance(item.provider.getAddress(false)).map {
                        bChannel.send(
                            item.copy(
                                balance = ResultStatus.Success(
                                    it.toBigDecimal().downDecimal(item.coinInfo.decimal)
                                )
                            )
                        )
                    }.catch {
                        bChannel.send(item.copy(balance = ResultStatus.Success(BigDecimal.ZERO)))
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
                        assets.postValue(_assets)
                    }
                }
            }
        }
    }

    private fun updateItem(asset: Asset?) {
        asset?.also {
            _assets.indexOfFirst { asset.coinInfo == it.coinInfo }.let {
                if (it >= 0) _assets[it] = asset
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        bChannel.close()
    }
}
