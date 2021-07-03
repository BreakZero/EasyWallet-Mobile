package com.easy.wallet.feature.settings

import com.easy.framework.base.BaseViewModel
import com.easy.wallet.data.DeFiWalletSDK
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent

@KoinApiExtension
class SettingsViewModel : BaseViewModel(), KoinComponent {

    fun initState(callback: (String) -> Unit) {
        callback.invoke(DeFiWalletSDK.chainId().name)
    }
}
