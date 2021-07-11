package com.easy.wallet.feature.home

import androidx.fragment.app.setFragmentResultListener
import com.easy.framework.base.BaseFragment
import com.easy.framework.delegate.viewBinding
import com.easy.framework.model.ResultStatus
import com.easy.wallet.R
import com.easy.wallet.ShowQrCodeDirections
import com.easy.wallet.databinding.FragmentHomeBinding
import com.easy.wallet.ext.hideLoading
import com.easy.wallet.ext.start
import com.easy.wallet.feature.home.adapter.AssetController
import com.google.android.material.appbar.MaterialToolbar
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    companion object {
        const val SET_RESULT_CODE = "setup-list"
        const val KEY_SETUP_RESULT = "setup-result"
    }

    override fun ownerToolbar(): MaterialToolbar? = null

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel: HomeViewModel by stateViewModel()

    private val assetController by lazy {
        AssetController(
            onItemClick = {
                val actionDetail =
                    HomeFragmentDirections.actionHomeToTransactionHistory(it.coinInfo)
                start(actionDetail)
            },
            onReceive = {
                val action = ShowQrCodeDirections.actionShowQrcode(it)
                start(action)
            }
        )
    }

    override fun setupView() {
        super.setupView()
        setTitle(getString(R.string.app_name))
        inflateMenu(R.menu.menu_home) {
            start(R.id.action_home_to_coin_list)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadBalances(true)
        }
        viewModel.apply {
            assets.observe(this@HomeFragment) {
                it?.let {
                    if (it.none { it.balance == ResultStatus.Loading }) {
                        hideLoading()
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
            viewModel.loadBalances(isChanged)
        }
    }
}
