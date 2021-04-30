package com.dougie.wallet.feature.coin

import androidx.lifecycle.liveData
import com.dougie.framework.base.BaseViewModel
import com.dougie.wallet.data.DeFiWalletSDK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

class CoinListViewModel : BaseViewModel() {
    fun loadLocalData() = liveData {
        emit(DeFiWalletSDK.allAssets())
    }

    private val changeHolder = hashMapOf<String, Int>()

    fun onChange(symbol: String) {
        changeHolder[symbol]?.plus(1) ?: kotlin.run { changeHolder[symbol] = 1 }
    }

    @ExperimentalCoroutinesApi
    fun done() = liveData {
        if (changeHolder.isEmpty()) {
            emit(false)
        } else {
            flow {
                changeHolder.forEach {
                    emit(it)
                }
            }.filter {
                it.value % 2 == 1
            }.map {
                it.key
            }.onCompletion {
                emit(true)
            }.collect {
                DeFiWalletSDK.toggleBySymbol(it)
            }
        }
    }
}
