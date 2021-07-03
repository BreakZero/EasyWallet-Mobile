package com.easy.wallet.feature.home

import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.dougie.framework.base.BaseFragment
import com.dougie.framework.delegate.viewBinding
import com.dougie.framework.model.ResultStatus
import com.dougie.wallet.R
import com.dougie.wallet.ShowQrCodeDirections
import com.dougie.wallet.databinding.FragmentHomeBinding
import com.dougie.wallet.feature.home.adapter.AssetController
import com.google.android.material.appbar.MaterialToolbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.androidx.viewmodel.scope.emptyState
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    companion object {
        const val SET_RESULT_CODE = "setup-list"
        const val KEY_SETUP_RESULT = "setup-result"
    }

    override fun ownerToolbar(): MaterialToolbar? = null

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel: HomeViewModel by viewModel(state = emptyState()) { parametersOf("ETH") }

    private val assetController by lazy {
        AssetController(
            onItemClick = {
                val actionDetail =
                    HomeFragmentDirections.actionHomeToTransactionHistory(it.coinInfo)
                findNavController().navigate(actionDetail)
            },
            onReceive = {
                val action = ShowQrCodeDirections.actionShowQrcode(it)
                findNavController().navigate(action)
            }
        )
    }

    override fun setupView() {
        super.setupView()
        setTitle("Wallet")
        inflateMenu(R.menu.menu_home) {
            findNavController().navigate(R.id.action_home_to_coin_list)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadBalances(true)
        }

        viewModel.apply {
            assets.observe(this@HomeFragment) {
                it?.let {
                    if (it.none { it.balance == ResultStatus.Loading }) {
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                    assetController.setData(it)
                } ?: kotlin.run {
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
            binding.swipeRefreshLayout.isRefreshing = true
            loadBalances(isRefresh = false)
        }
        binding.rvHomeAsset.setController(assetController)

        setFragmentResultListener(SET_RESULT_CODE) { _, bundle ->
            val isChanged = bundle.getBoolean(KEY_SETUP_RESULT)
            Timber.d("===> $isChanged ")
            viewModel.loadBalances(isChanged)
        }
    }
}
