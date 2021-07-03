package com.easy.wallet.feature

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.easy.framework.base.BaseViewModel
import com.easy.wallet.constant.StoreKey
import com.easy.wallet.data.DeFiWalletSDK
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

@KoinApiExtension
class MainViewModel : BaseViewModel(), KoinComponent {
    private val sharedPres = get<SharedPreferences>()

    private val _walletExist = MutableLiveData<Boolean>()
    val walletExist = Transformations.map(_walletExist) { it }

    fun initWalletSuccess() {
        _walletExist.value = true
    }

    fun fetch() {
        val localMnemonic = sharedPres.getString(StoreKey.KEY_MNEMONIC, "").orEmpty()
        if (localMnemonic.isNotBlank()) DeFiWalletSDK.initWallet(localMnemonic)
        _walletExist.value = localMnemonic.isNotBlank()
    }
}
