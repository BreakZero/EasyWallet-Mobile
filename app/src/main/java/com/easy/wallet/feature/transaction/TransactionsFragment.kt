package com.easy.wallet.feature.transaction

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.easy.framework.base.BaseFragment
import com.easy.framework.delegate.viewBinding
import com.easy.framework.ext.observeState
import com.easy.framework.ext.onSingleClick
import com.easy.wallet.R
import com.easy.wallet.ShowQrCodeDirections
import com.easy.wallet.databinding.FragmentTransactionHistoryBinding
import com.easy.wallet.ext.start
import com.easy.wallet.feature.transaction.adapter.TransactionHistoryController
import com.google.android.material.appbar.MaterialToolbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class TransactionsFragment : BaseFragment(R.layout.fragment_transaction_history) {
    private val binding by viewBinding(FragmentTransactionHistoryBinding::bind)
    private val args: TransactionsFragmentArgs by navArgs()

    override fun ownerToolbar(): MaterialToolbar? = null

    private val viewModel by viewModel<TransactionsViewModel> {
        parametersOf(args.currencyInfo)
    }

    private val transactionHistoryController by lazy {
        TransactionHistoryController {
            start(R.id.action_to_detail)
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
            viewModel.loadTransactions()
        }
        binding.btnReceive.onSingleClick(lifecycleScope) {
            val action = ShowQrCodeDirections.actionShowQrcode(viewModel.address())
            start(action)
        }
    }

    override fun applyViewModel() {
        super.applyViewModel()
        viewModel.apply {
            transactions.observeState(
                this@TransactionsFragment,
                onError = {
                    binding.refreshLayout.isRefreshing = false
                    Timber.e(getString(R.string.error_somethings_went_wrong))
                },
                onSuccess = {
                    binding.refreshLayout.isRefreshing = false
                    transactionHistoryController.setData(it)
                },
                onLoading = {
                    binding.refreshLayout.isRefreshing = true
                }
            )
            loadTransactions()
        }
    }
}
