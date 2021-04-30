package com.dougie.wallet.feature.sharing

import android.graphics.Bitmap
import android.graphics.Color
import androidx.navigation.fragment.navArgs
import com.dougie.framework.base.BaseFragment
import com.dougie.framework.delegate.viewBinding
import com.dougie.wallet.R
import com.dougie.wallet.databinding.FragmentQrCodeBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import org.koin.core.component.bind

class QRCodeFragment : BaseFragment(R.layout.fragment_qr_code) {
    private val args: QRCodeFragmentArgs by navArgs()
    private val binding by viewBinding(FragmentQrCodeBinding::bind)

    override fun ownerToolbar(): MaterialToolbar? = null

    override fun setupView() {
        super.setupView()

        binding.tvQRCodeContent.text = args.content
        genQRCode(args.content) {
            binding.ivQRCode.setImageBitmap(it)
        }
    }

    private inline fun genQRCode(content: String, using: (Bitmap) -> Unit) {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        using(bitmap)
    }
}
