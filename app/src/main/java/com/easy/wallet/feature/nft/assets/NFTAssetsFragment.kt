package com.easy.wallet.feature.nft.assets

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.easy.framework.base.BaseFragment
import com.easy.framework.delegate.viewBinding
import com.easy.wallet.R
import com.easy.wallet.data.param.NFTAssetParameter
import com.easy.wallet.databinding.FragmentNftAssetListBinding
import com.easy.wallet.feature.nft.assets.adapter.NFTAssetsController
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class NFTAssetsFragment : BaseFragment(R.layout.fragment_nft_asset_list) {
    override fun ownerToolbar(): MaterialToolbar? = null

    private val args: NFTAssetsFragmentArgs by navArgs()

    private val viewModel by viewModel<NFTAssetsViewModel>()

    private val binding by viewBinding(FragmentNftAssetListBinding::bind)
    private val listController by lazy {
        NFTAssetsController {
            val actionToAsset =
                NFTAssetsFragmentDirections.actionToAssetDetail(
                    NFTAssetParameter(
                        contractAddress = it.contractAddress,
                        tokenId = it.tokenId,
                        permalink = it.permalink
                    )
                )
            findNavController().navigate(actionToAsset)
        }.apply {
            spanCount = 2
        }
    }

    override fun setupView() {
        super.setupView()
        setTitle(String.format(getString(R.string.text_nft_collection), args.collectionInfo.name))

        val gridLayoutManager = GridLayoutManager(requireContext(), 2).apply {
            spanSizeLookup = listController.spanSizeLookup
        }
        binding.gridAssets.layoutManager = gridLayoutManager

        binding.gridAssets.setController(listController)
        binding.nftAssetSwipeRefreshLayout.setOnRefreshListener {
            viewModel.loadAssets(args.collectionInfo)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.loadAssets(args.collectionInfo)
            binding.nftAssetSwipeRefreshLayout.isRefreshing = true
            viewModel.assets().collect {
                binding.nftAssetSwipeRefreshLayout.isRefreshing = it.isEmpty()
                listController.setData(it)
            }
        }
    }
}
