package com.easy.wallet.feature.transaction

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dougie.framework.base.BaseFragment
import com.dougie.framework.delegate.viewBinding
import com.dougie.framework.ext.observeState
import com.dougie.framework.ext.onSingleClick
import com.dougie.wallet.R
import com.dougie.wallet.data.provider.IProvider
import com.dougie.wallet.databinding.FragmentTransactionHistoryBinding
import com.dougie.wallet.feature.transaction.adapter.TransactionHistoryController
import com.dougie.wallet.koin.ScopeConst
import com.google.android.material.appbar.MaterialToolbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import timber.log.Timber

@KoinApiExtension
class TransactionFragment : BaseFragment(R.layout.fragment_transaction_history) {
    private val binding by viewBinding(FragmentTransactionHistoryBinding::bind)
    private val args: TransactionFragmentArgs by navArgs()

    private val flowSession =
        getKoin().getOrCreateScope(ScopeConst.FLOW_SESSION_ID, named(ScopeConst.FLOW_SESSION_NAME))

    private val coinProvider by flowSession.inject<IProvider> {
        parametersOf(args.currencyInfo)
    }

    override fun ownerToolbar(): MaterialToolbar? = null

    private val viewModel by viewModel<TransactionViewModel> {
        parametersOf(coinProvider)
    }

    private val transactionHistoryController by lazy {
        TransactionHistoryController {
            Timber.d(it.toString())
        }
    }

    override fun setupView() {
        super.setupView()
        scope.linkTo(flowSession)

        setTitle("${args.currencyInfo.name} Transactions")
        binding.btnSend.onSingleClick(lifecycleScope) {
            val info = TransactionFragmentDirections.actionToSend(args.currencyInfo)
            findNavController().navigate(info)
        }
        binding.rvTransactionHistory.setController(transactionHistoryController)
    }

    override fun applyViewModel() {
        super.applyViewModel()
        viewModel.apply {
            transactions.observeState(
                this@TransactionFragment,
                onError = {
                    binding.refreshLayout.isRefreshing = false
                    Timber.e("somethings wrong")
                },
                onSuccess = {
                    Timber.d("completed")
                    binding.refreshLayout.isRefreshing = false
                    transactionHistoryController.setData(it)
                },
                onLoading = {
                    Timber.d("loading")
                    binding.refreshLayout.isRefreshing = true
                }
            )
            loadTransactions()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        flowSession.close()
    }
}
