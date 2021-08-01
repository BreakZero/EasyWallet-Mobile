package com.easy.wallet.feature.transaction

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.easy.framework.base.BaseFragment
import com.easy.framework.delegate.viewBinding
import com.easy.framework.ext.onSingleClick
import com.easy.wallet.R
import com.easy.wallet.ShowQrCodeDirections
import com.easy.wallet.databinding.FragmentTransactionHistoryBinding
import com.easy.wallet.ext.start
import com.easy.wallet.feature.transaction.adapter.TransactionHistoryController
import com.easy.wallet.feature.transaction.uimodel.TransactionsState
import com.google.android.material.appbar.MaterialToolbar
import io.uniflow.android.livedata.onEvents
import io.uniflow.android.livedata.onStates
import io.uniflow.core.flow.data.UIEvent
import io.uniflow.core.flow.data.UIState
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TransactionsFragment : BaseFragment(R.layout.fragment_transaction_history) {
    private val binding by viewBinding(FragmentTransactionHistoryBinding::bind)
    private val args: TransactionsFragmentArgs by navArgs()

    override fun ownerToolbar(): MaterialToolbar? = null

    private val viewModel by viewModel<TransactionsViewModel> {
        parametersOf(args.currencyInfo)
    }

    private val transactionHistoryController by lazy {
        TransactionHistoryController {
            val action = TransactionsFragmentDirections.actionToDetail(it)
            start(action)
        }
    }

    override fun setupView() {
        super.setupView()

        setTitle("${args.currencyInfo.name} Transactions")
    }

    override fun initEvents() {
        binding.btnSend.onSingleClick(lifecycleScope) {
            val info = TransactionsFragmentDirections.actionToSend(args.currencyInfo)
            start(info)
        }
        binding.rvTransactionHistory.setController(transactionHistoryController)
        binding.refreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
        binding.btnReceive.onSingleClick(lifecycleScope) {
            val action = ShowQrCodeDirections.actionShowQrcode(viewModel.address())
            start(action)
        }

        onEvents(viewModel) {
            when(it) {
                is UIEvent.Loading -> {
                    binding.refreshLayout.isRefreshing = true
                }
            }
        }

        onStates(viewModel) {
            when(it) {
                is TransactionsState -> {
                    binding.refreshLayout.isRefreshing = false
                    transactionHistoryController.setData(it.list)
                }
                is UIState.Failed -> {
                    binding.refreshLayout.isRefreshing = false
                }
                is UIState.Loading -> {
                    binding.refreshLayout.isRefreshing = true
                }
            }
        }
    }
}
