package com.dougie.wallet.feature.transaction

import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.dougie.framework.base.BaseViewModel
import com.dougie.framework.common.StatefulMutableLiveData
import com.dougie.framework.model.RequestState
import com.dougie.wallet.data.data.model.TransactionDataModel
import com.dougie.wallet.data.provider.IProvider
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import timber.log.Timber

@KoinApiExtension
class TransactionViewModel(
    private val coinProvider: IProvider
) : BaseViewModel(), KoinComponent {
    private var offset: Int = 0
    private val _transactions = StatefulMutableLiveData<List<TransactionDataModel>>()
    val transactions = Transformations.map(_transactions) { it }

    @FlowPreview
    fun loadTransactions(isRefresh: Boolean = true) {
        if (isRefresh) offset = 0
        coinProvider.getTransactions(
            coinProvider.getAddress(false),
            10,
            offset
        ).onStart {
            _transactions.value = RequestState.Loading
        }.catch {
            Timber.e(it)
            _transactions.value = RequestState.Error(error = it)
        }.onEach {
            _transactions.value = RequestState.Success(it)
        }.launchIn(viewModelScope)
    }
}
