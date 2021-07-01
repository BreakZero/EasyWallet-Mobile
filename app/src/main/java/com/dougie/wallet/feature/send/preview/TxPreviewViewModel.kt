package com.dougie.wallet.feature.send.preview

import androidx.lifecycle.viewModelScope
import com.dougie.framework.base.BaseViewModel
import com.dougie.wallet.data.provider.IProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class TxPreviewViewModel(
    private val coinProvider: IProvider
) : BaseViewModel() {
    fun broadcastTransaction(
        rawData: String,
        onSuccess: (String) -> Unit,
        onError: () -> Unit
    ) {
        coinProvider.broadcastTransaction(rawData)
            .flowOn(Dispatchers.IO)
            .catch {
                Timber.e(it)
                onError.invoke()
            }
            .onEach {
                onSuccess.invoke(it)
            }.launchIn(viewModelScope)
    }
}
