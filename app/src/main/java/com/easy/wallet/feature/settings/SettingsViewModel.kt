package com.easy.wallet.feature.settings

import com.easy.wallet.data.WalletDataSDK
import com.easy.wallet.feature.settings.uimodel.SettingsState
import io.uniflow.android.AndroidDataFlow
import org.koin.core.component.KoinComponent

class SettingsViewModel : AndroidDataFlow(), KoinComponent {
  init {
    action {
      setState { SettingsState(WalletDataSDK.chainId().name) }
    }
  }
}
