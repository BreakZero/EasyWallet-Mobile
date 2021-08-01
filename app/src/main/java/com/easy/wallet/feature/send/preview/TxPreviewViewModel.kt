package com.easy.wallet.feature.send.preview

import androidx.lifecycle.viewModelScope
import com.easy.framework.base.BaseViewModel
import com.easy.wallet.data.CurrencyInfo
import com.easy.wallet.data.WalletDataSDK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class TxPreviewViewModel(
    currencyInfo: CurrencyInfo
) : BaseViewModel() {
    private val coinProvider =
        WalletDataSDK.injectProvider(currencyInfo.slug, currencyInfo.symbol, currencyInfo.decimal)
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
            }.onEach {
                onSuccess.invoke(it)
            }.launchIn(viewModelScope)
    }
}
