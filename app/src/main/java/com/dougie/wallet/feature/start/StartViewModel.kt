package com.dougie.wallet.feature.start

import android.content.SharedPreferences
import com.dougie.framework.base.BaseViewModel
import com.dougie.framework.model.ResultStatus
import com.dougie.wallet.constant.StoreKey
import com.dougie.wallet.data.DeFiWalletSDK
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

@KoinApiExtension
class StartViewModel : BaseViewModel(), KoinComponent {
    private val sharedPres = get<SharedPreferences>()

    private val walletExist = MutableStateFlow<ResultStatus<Boolean>>(ResultStatus.Loading)

    fun initWalletSuccess() {
        walletExist.value = ResultStatus.Success(true)
    }

    fun fetch() {
        val localMnemonic = sharedPres.getString(StoreKey.KEY_MNEMONIC, "").orEmpty()
        if (localMnemonic.isNotBlank()) DeFiWalletSDK.initWallet(localMnemonic)
        walletExist.value = ResultStatus.Success(localMnemonic.isNotBlank())
    }

    fun importState() = walletExist
}
