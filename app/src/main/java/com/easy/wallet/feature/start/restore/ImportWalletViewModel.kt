package com.easy.wallet.feature.start.restore

import androidx.lifecycle.viewModelScope
import com.easy.framework.base.BaseViewModel
import com.easy.wallet.data.WalletDataSDK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent

class ImportWalletViewModel : BaseViewModel(), KoinComponent {
    private val words = mutableListOf<String>()

    fun addWord(word: String) {
        words.add(word)
    }

    fun deleteLast() {
        words.removeLast()
    }

    fun done(callback: (Boolean) -> Unit) {
        val mnemonic = words.joinToString(" ")

        flow {
            val importResult =
                WalletDataSDK.injectWallet(walletName = "Wallet 1", mnemonic = mnemonic)
            emit(importResult)
        }.onEach {
            callback.invoke(it)
        }.launchIn(viewModelScope)
    }
}
