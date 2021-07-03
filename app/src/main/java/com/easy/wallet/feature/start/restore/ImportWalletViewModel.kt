package com.easy.wallet.feature.start.restore

import android.content.SharedPreferences
import androidx.core.content.edit
import com.easy.framework.base.BaseViewModel
import com.easy.wallet.constant.StoreKey
import com.easy.wallet.data.DeFiWalletSDK
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

@KoinApiExtension
class ImportWalletViewModel : BaseViewModel(), KoinComponent {
    private val sharedPres = get<SharedPreferences>()
    private val words = mutableListOf<String>()

    fun addWord(word: String) {
        words.add(word)
    }

    fun deleteLast() {
        words.removeLast()
    }

    fun done(callback: () -> Unit) {
        val mnemonic = words.joinToString(" ") {
            if (it.endsWith(",")) it.removeSuffix(",")
            else it
        }
        sharedPres.edit {
            putString(StoreKey.KEY_MNEMONIC, mnemonic)
        }
        DeFiWalletSDK.initWallet(mnemonic = mnemonic)
        callback.invoke()
    }
}
