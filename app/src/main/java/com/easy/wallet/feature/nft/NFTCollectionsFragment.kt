package com.easy.wallet.feature.nft

import androidx.lifecycle.lifecycleScope
import com.easy.framework.base.BaseFragment
import com.easy.framework.delegate.viewBinding
import com.easy.wallet.R
import com.easy.wallet.databinding.FragmentNftIndexBinding
import com.easy.wallet.ext.start
import com.easy.wallet.feature.nft.adapter.NFTCollectionController
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class NFTCollectionsFragment : BaseFragment(R.layout.fragment_nft_index) {
  override fun ownerToolbar(): MaterialToolbar? = null

  private val listController by lazy {
    NFTCollectionController {
      val actionToAssets =
        NFTCollectionsFragmentDirections.actionToCollectionAssets(it)
      start(actionToAssets)
    }
  }

  private val viewModel by viewModel<NFTCollectionsViewModel>()

  private val binding by viewBinding(FragmentNftIndexBinding::bind)

  override fun setupView() {
    super.setupView()
    setTitle(getString(R.string.text_nft_collections))

    binding.gridCollections.setController(listController)
    binding.nftSwipeRefreshLayout.setOnRefreshListener {
      viewModel.loadCollections()
    }

    lifecycleScope.launchWhenStarted {
      viewModel.loadCollections()
      viewModel.observeState().collect {
        binding.nftSwipeRefreshLayout.isRefreshing = it.loading
        listController.setData(it.data)
      }
    }
  }
}
