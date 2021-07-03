package com.easy.wallet.feature.defi

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import coil.load
import com.dougie.framework.base.BaseFragment
import com.dougie.framework.delegate.viewBinding
import com.dougie.framework.ext.onSingleClick
import com.dougie.wallet.R
import com.dougie.wallet.databinding.FragmentDefiIndexBinding
import com.google.android.material.appbar.MaterialToolbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class TestDefiFragment : BaseFragment(R.layout.fragment_defi_index) {
    override fun ownerToolbar(): MaterialToolbar? = null

    private val viewModel by viewModel<TestDefiViewModel>()

    private val binding by viewBinding(FragmentDefiIndexBinding::bind)

    override fun setupView() {
        super.setupView()
        updateStatusBarColor(ActivityCompat.getColor(requireContext(), R.color.md_white_1000))

        binding.ivNFTAssetImage.load("https://lh3.googleusercontent.com/0sIks4cV4hPPXAyA0KFkT32osPX4uQJmifFkIDDGvEkYtdp_R9EaUiJ0SL-HJK-57wW4tPjt7689nMVn_EoV-KNwcCP2RDSbVeDLNw")

        binding.buttonGen.onSingleClick(lifecycleScope) {
            binding.ivPreview.setImageBitmap(createBitmap(binding.assetCardView))
        }
    }

    private fun createBitmap(view: View): Bitmap {
        val width = view.width
        val height = view.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}
