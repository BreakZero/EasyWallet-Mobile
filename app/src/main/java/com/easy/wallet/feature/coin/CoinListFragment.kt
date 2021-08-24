package com.easy.wallet.feature.coin

import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.easy.framework.base.BaseFragment
import com.easy.framework.delegate.viewBinding
import com.easy.wallet.R
import com.easy.wallet.databinding.FragmentCoinListBinding
import com.easy.wallet.feature.coin.adapter.CoinController
import com.easy.wallet.feature.coin.uimodel.SetCoinUIEvent
import com.easy.wallet.feature.coin.uimodel.SupportCoinState
import com.easy.wallet.feature.home.HomeFragment
import com.google.android.material.appbar.MaterialToolbar
import io.uniflow.android.livedata.onEvents
import io.uniflow.android.livedata.onStates
import io.uniflow.core.flow.data.UIState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class CoinListFragment : BaseFragment(R.layout.fragment_coin_list) {
  private val binding by viewBinding(FragmentCoinListBinding::bind)
  private val viewModel: CoinListViewModel by viewModel()

  override fun ownerToolbar(): MaterialToolbar? = null

  private val coinController by lazy {
    CoinController {
      viewModel.onChange(it)
    }
  }

  @ExperimentalCoroutinesApi
  override fun setupView() {
    super.setupView()
    setTitle("Coin List")
    inflateMenu(R.menu.menu_done) {
      viewModel.done()
    }

    binding.rvCoinList.setController(coinController)

    onStates(viewModel) {
      when (it) {
        is SupportCoinState -> {
          coinController.setData(it.list)
        }
        is UIState.Failed -> {
          Timber.d("====== ${it.message.orEmpty()}")
        }
      }
    }
  }

  override fun initEvents() {
    super.initEvents()
    onEvents(viewModel) {
      when (it) {
        is SetCoinUIEvent.UpdateState -> {
          Timber.d("====== state updated")
        }
        is SetCoinUIEvent.DoneEvent -> {
          setFragmentResult(
            HomeFragment.SET_RESULT_CODE,
            bundleOf(HomeFragment.KEY_SETUP_RESULT to it.result)
          )
          findNavController().navigateUp()
        }
      }
    }
  }
}
