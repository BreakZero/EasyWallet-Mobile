package com.easy.wallet.feature.start.splash

import androidx.lifecycle.viewModelScope
import com.easy.framework.base.BaseViewModel
import com.easy.framework.model.ResultStatus
import com.easy.wallet.data.WalletDataSDK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class SplashViewModel : BaseViewModel(), KoinComponent {
    private val walletExist = MutableStateFlow<ResultStatus<Boolean>>(ResultStatus.Loading)

    fun initWalletSuccess() {
        walletExist.value = ResultStatus.Success(true)
    }

    fun fetch() {
        viewModelScope.launch(Dispatchers.IO) {
            walletExist.value = ResultStatus.Success(
                WalletDataSDK.injectWallet()?.let {
                    true
                } ?: false
            )
        }
    }

    fun importState() = walletExist
}
