package com.easy.wallet.feature.coin

import androidx.lifecycle.liveData
import com.easy.framework.base.BaseViewModel
import com.easy.wallet.data.WalletDataSDK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

class CoinListViewModel : BaseViewModel() {
    fun loadLocalData() = liveData {
        emit(WalletDataSDK.allAssets())
    }

    private val changeHolder = hashMapOf<String, Int>()

    fun onChange(slug: String) {
        changeHolder[slug]?.plus(1) ?: kotlin.run { changeHolder[slug] = 1 }
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
                WalletDataSDK.toggleBySlug(it)
            }
        }
    }
}
