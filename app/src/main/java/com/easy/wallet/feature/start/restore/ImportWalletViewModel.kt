package com.easy.wallet.feature.start.restore

import com.easy.wallet.data.WalletDataSDK
import io.uniflow.android.AndroidDataFlow
import io.uniflow.core.flow.data.UIState
import org.koin.core.component.KoinComponent

class ImportWalletViewModel : AndroidDataFlow(), KoinComponent {
    private val words = mutableListOf<String>()

    fun addWord(word: String) {
        words.add(word)
    }

    fun deleteLast() {
        words.removeLast()
    }

    fun done() = action(
        onAction = {
            val mnemonic = words.joinToString(" ")
            val result = WalletDataSDK.injectWallet(walletName = "Wallet 1", mnemonic = mnemonic)
            setState {
                if (result) UIState.Success
                else UIState.Failed()
            }
        },
        onError = { error, _ ->
            setState { UIState.Failed(error = error) }
        }
    )
}
