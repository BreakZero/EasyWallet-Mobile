package com.easy.wallet.feature.transaction

import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.easy.framework.base.BaseViewModel
import com.easy.framework.common.StatefulMutableLiveData
import com.easy.framework.model.RequestState
import com.easy.wallet.data.data.model.TransactionDataModel
import com.easy.wallet.data.provider.IProvider
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import org.koin.core.component.KoinComponent
import timber.log.Timber

class TransactionsViewModel(
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

    fun address() = coinProvider.getAddress(false)
}