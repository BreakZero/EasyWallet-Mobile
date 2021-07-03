package com.easy.wallet.feature.transaction

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.easy.framework.base.BaseFragment
import com.easy.framework.delegate.viewBinding
import com.easy.framework.ext.observeState
import com.easy.framework.ext.onSingleClick
import com.easy.wallet.R
import com.easy.wallet.databinding.FragmentTransactionHistoryBinding
import com.easy.wallet.feature.transaction.adapter.TransactionHistoryController
import com.google.android.material.appbar.MaterialToolbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class TransactionFragment : BaseFragment(R.layout.fragment_transaction_history) {
    private val binding by viewBinding(FragmentTransactionHistoryBinding::bind)
    private val args: TransactionFragmentArgs by navArgs()

    override fun ownerToolbar(): MaterialToolbar? = null

    private val viewModel by viewModel<TransactionViewModel> {
        parametersOf(args.currencyInfo)
    }

    private val transactionHistoryController by lazy {
        TransactionHistoryController {
            Timber.d(it.toString())
        }
    }

    override fun setupView() {
        super.setupView()

        setTitle("${args.currencyInfo.name} Transactions")
        binding.btnSend.onSingleClick(lifecycleScope) {
            val info = TransactionFragmentDirections.actionToSend(args.currencyInfo)
            findNavController().navigate(info)
        }
        binding.rvTransactionHistory.setController(transactionHistoryController)
        binding.refreshLayout.setOnRefreshListener {
            viewModel.loadTransactions()
        }
    }

    override fun applyViewModel() {
        super.applyViewModel()
        viewModel.apply {
            transactions.observeState(
                this@TransactionFragment,
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
