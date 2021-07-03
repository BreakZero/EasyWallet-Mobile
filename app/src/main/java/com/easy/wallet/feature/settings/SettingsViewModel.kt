package com.easy.wallet.feature.settings

import com.easy.framework.base.BaseViewModel
import com.easy.wallet.data.WalletDataSDK
import org.koin.core.component.KoinComponent

class SettingsViewModel : BaseViewModel(), KoinComponent {

    fun initState(callback: (String) -> Unit) {
        callback.invoke(WalletDataSDK.chainId().name)
    }
}
