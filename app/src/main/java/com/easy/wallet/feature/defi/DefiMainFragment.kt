package com.easy.wallet.feature.defi

import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.easy.framework.base.BaseFragment
import com.easy.framework.delegate.viewBinding
import com.easy.wallet.R
import com.easy.wallet.ShowDappDirections
import com.easy.wallet.databinding.FragmentDefiIndexBinding
import com.easy.wallet.ext.start
import com.easy.wallet.feature.defi.adapter.DAppController
import com.easy.wallet.feature.defi.uimodels.DeFiListState
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
        val direction = ShowDappDirections.actionToDapp(it)
        start(directions = direction)
      }
    }
  }

  override fun setupView() {
    super.setupView()
    updateStatusBarColor(ActivityCompat.getColor(requireContext(), R.color.md_white_1000))
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