package com.easy.wallet.feature.defi

import androidx.lifecycle.lifecycleScope
import com.easy.framework.base.BaseFragment
import com.easy.framework.delegate.viewBinding
import com.easy.wallet.R
import com.easy.wallet.ShowDappDirections
import com.easy.wallet.databinding.FragmentDefiIndexBinding
import com.easy.wallet.ext.start
import com.easy.wallet.feature.defi.adapter.DAppController
import com.easy.wallet.feature.defi.uimodels.DeFiListState
import com.easy.wallet.feature.sharing.dapp.DAppConnectInfo
import com.google.android.material.appbar.MaterialToolbar
import io.uniflow.android.livedata.onStates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DefiMainFragment : BaseFragment(R.layout.fragment_defi_index) {
  override fun ownerToolbar(): MaterialToolbar? = null

  private val viewModel by viewModel<DefiMainViewModel>()

  private val binding by viewBinding(FragmentDefiIndexBinding::bind)

  private val dappController by lazy {
    DAppController {
      lifecycleScope.launch(Dispatchers.Main) {
        val appInfo = DAppConnectInfo(
          chainId = it.chainId,
          rpc = it.rpcUrl,
          appUrl = it.link
        )
        val direction = ShowDappDirections.actionToDapp(appInfo)
        start(directions = direction)
      }
    }
  }

  override fun setupView() {
    super.setupView()
    setTitle(getString(R.string.defi))
    binding.defiList.setController(dappController)
  }

  override fun initEvents() {
    super.initEvents()
    onStates(viewModel) {
      when(it) {
        is DeFiListState -> {
          dappController.setData(it.dapps)
        }
      }
    }
  }
}
