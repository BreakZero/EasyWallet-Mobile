package com.easy.wallet.feature.transaction

import com.easy.wallet.data.provider.IProvider
import com.easy.wallet.feature.transaction.uimodel.TransactionsState
import io.uniflow.android.AndroidDataFlow
import io.uniflow.core.coroutines.onFlow
import io.uniflow.core.flow.data.UIEvent
import io.uniflow.core.flow.data.UIState
import org.koin.core.component.KoinComponent
import timber.log.Timber

class TransactionsViewModel(
    private val coinProvider: IProvider
) : AndroidDataFlow(), KoinComponent {
    companion object {
        private const val PAGE_LIMIT = 10
    }

    private var offset: Int = 0

    init {
        refresh()
    }

    private suspend fun loadTransactions() {
        val flow = coinProvider.getTransactions(
            coinProvider.getAddress(false),
            PAGE_LIMIT,
            offset
        )
        onFlow(
            flow = { flow },
            doAction = {
                setState { TransactionsState(it) }
            },
            onError = { error, _ ->
                Timber.e(error)
                setState { UIState.Failed() }
            }
        )
    }

    fun refresh() = action {
        offset = 0
        sendEvent(UIEvent.Loading)
        loadTransactions()
    }

    fun loadMore() = action {
        offset += PAGE_LIMIT * (offset + 1)
        loadTransactions()
    }

    fun address() = coinProvider.getAddress(false)
}
