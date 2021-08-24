package com.easy.wallet.feature.start.splash

import com.easy.wallet.data.WalletDataSDK
import io.uniflow.android.AndroidDataFlow
import io.uniflow.core.flow.data.UIState
import org.koin.core.component.KoinComponent
import timber.log.Timber

class SplashViewModel : AndroidDataFlow(), KoinComponent {
  init {
    action(
      onAction = {
        val result = WalletDataSDK.injectWallet()
        setState {
          result?.let {
            UIState.Success
          } ?: UIState.Failed()
        }
      },
      onError = { error, _ ->
        Timber.e(error)
        setState { UIState.Failed(error = error) }
      }
    )
  }
}
