package com.easy.wallet.feature.nft.asset

import android.content.Intent
import androidx.core.app.ActivityCompat
import androidx.core.app.ShareCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.load
import com.easy.framework.base.BaseFragment
import com.easy.framework.delegate.viewBinding
import com.easy.wallet.R
import com.easy.wallet.databinding.FragmentNftAssetBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class NFTAssetFragment : BaseFragment(R.layout.fragment_nft_asset) {
    private val args: NFTAssetFragmentArgs by navArgs()
    private val viewModel by viewModel<NFTAssetViewModel>()
    private val binding by viewBinding(FragmentNftAssetBinding::bind)

    private var player: SimpleExoPlayer? = null

    override fun ownerToolbar(): MaterialToolbar = binding.toolbar

    override fun setupView() {
        super.setupView()
        setTitle(getString(R.string.text_nft))
        inflateMenu(R.menu.menu_nft_detail) {
            viewModel.send()
            createShareIntent()
        }
        updateStatusBarColor(ActivityCompat.getColor(requireContext(), R.color.zxing_transparent))

        lifecycleScope.launchWhenStarted {
            viewModel.loadAssetDetail(args.nftAssetParam)
            viewModel.asset().collect {
                binding.ivNFTAssetImage.load(it.imagePreviewUrl)
                binding.tvAssetDescription.text = it.description.orEmpty()
                binding.tvNFTAssetName.text = it.name

                it.animationUrl?.let {
                    player?.setMediaItem(MediaItem.fromUri(it))
                    player?.prepare()
                }
            }
        }
        initializePlayer()
    }

    private fun initializePlayer() {
        val mediaSourceFactory = DefaultMediaSourceFactory(requireContext())
            .setAdViewProvider(binding.videoPreview)
        player = SimpleExoPlayer.Builder(requireContext())
            .setMediaSourceFactory(mediaSourceFactory)
            .build()
        binding.videoPreview.player = player
    }

    override fun onStart() {
        super.onStart()
        binding.videoPreview.onResume()
    }

    override fun onPause() {
        binding.videoPreview.onPause()
        player?.release()
        super.onPause()
    }

    private fun createShareIntent() {
        val shareText = args.nftAssetParam.permalink
        val shareIntent = ShareCompat.IntentBuilder.from(requireActivity())
            .setText(shareText)
            .setType("text/plain")
            .createChooserIntent()
            .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        startActivity(shareIntent)
    }
}
