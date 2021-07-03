package com.easy.wallet.feature.coin

import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.easy.framework.base.BaseFragment
import com.easy.framework.delegate.viewBinding
import com.easy.wallet.R
import com.easy.wallet.databinding.FragmentCoinListBinding
import com.easy.wallet.feature.coin.adapter.CoinController
import com.easy.wallet.feature.home.HomeFragment
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

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
            viewModel.done().observe(
                this@CoinListFragment,
                {
                    setFragmentResult(
                        HomeFragment.SET_RESULT_CODE,
                        bundleOf(HomeFragment.KEY_SETUP_RESULT to it)
                    )
                    findNavController().navigateUp()
                }
            )
        }

        binding.rvCoinList.setController(coinController)
        viewModel.apply {
            loadLocalData().observe(
                this@CoinListFragment,
                {
                    coinController.setData(it)
                }
            )
        }
    }
}