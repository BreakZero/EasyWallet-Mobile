package com.easy.wallet.feature.sharing

import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dougie.framework.base.BaseFragment
import com.dougie.framework.delegate.viewBinding
import com.dougie.framework.ext.onSingleClick
import com.dougie.wallet.R
import com.dougie.wallet.databinding.FragmentTabbedScanningBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.DefaultDecoderFactory

class ScannerFragment : BaseFragment(R.layout.fragment_tabbed_scanning) {
    private val binding by viewBinding(FragmentTabbedScanningBinding::bind)
    companion object {
        const val REQUEST_QR_CODE = "request-qr-code"
        const val KEY_QR_CODE = "key-qr-code"
    }

    override fun ownerToolbar(): MaterialToolbar? = null

    override fun setupView() {
        super.setupView()
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        binding.ivClose.onSingleClick(lifecycleScope) {
            findNavController().navigateUp()
        }

        binding.barcodeView.barcodeView.decoderFactory =
            DefaultDecoderFactory(listOf(BarcodeFormat.QR_CODE))
        binding.barcodeView.decodeSingle {
            setFragmentResult(
                REQUEST_QR_CODE,
                bundleOf(KEY_QR_CODE to (it.text.orEmpty()))
            )
            findNavController().navigateUp()
        }
        binding.barcodeView.setStatusText("Scan a barcode")
    }

    override fun onPause() {
        super.onPause()
        binding.barcodeView.pauseAndWait()
    }

    override fun onResume() {
        super.onResume()
        binding.barcodeView.resume()
    }
}
