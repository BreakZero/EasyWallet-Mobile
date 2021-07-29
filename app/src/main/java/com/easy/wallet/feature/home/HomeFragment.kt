package com.easy.wallet.feature.home

import androidx.fragment.app.setFragmentResultListener
import com.easy.framework.base.BaseFragment
import com.easy.framework.delegate.viewBinding
import com.easy.wallet.R
import com.easy.wallet.ShowQrCodeDirections
import com.easy.wallet.databinding.FragmentHomeBinding
import com.easy.wallet.ext.start
import com.easy.wallet.feature.home.adapter.AssetController
import com.easy.wallet.feature.home.uimodel.AssetListState
import com.easy.wallet.feature.home.uimodel.AssetListUIEvent
import com.google.android.material.appbar.MaterialToolbar
import io.uniflow.android.livedata.onEvents
import io.uniflow.android.livedata.onStates
import io.uniflow.core.flow.data.UIState
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import timber.log.Timber

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

    override fun initEvents() {
        super.initEvents()
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }

        onEvents(viewModel) {
            when(it) {
                AssetListUIEvent.RefreshEvent -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                    Timber.d("===== refresh")
                }
            }
        }
    }

    override fun setupView() {
        super.setupView()
        setTitle(getString(R.string.app_name))
        inflateMenu(R.menu.menu_home) {
            start(R.id.action_home_to_coin_list)
        }

        onStates(viewModel) {
            when(it) {
                is AssetListState -> {
                    if (it.assets.none { it.balance == null }) {
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                    assetController.setData(it.assets)
                }
                is UIState.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                is UIState.Failed -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    Timber.d("load balance failed")
                }
            }
        }

        viewModel.initBalances()

        binding.rvHomeAsset.setController(assetController)

        setFragmentResultListener(SET_RESULT_CODE) { _, bundle ->
            val isChanged = bundle.getBoolean(KEY_SETUP_RESULT)
            if (isChanged) viewModel.refresh()
        }
    }
}
