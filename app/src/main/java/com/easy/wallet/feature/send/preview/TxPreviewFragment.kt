package com.easy.wallet.feature.send.preview

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.easy.framework.base.BaseBottomSheetFragment
import com.easy.framework.delegate.viewBinding
import com.easy.framework.ext.onSingleClick
import com.easy.wallet.R
import com.easy.wallet.biometric.BiometricPromptUtils
import com.easy.wallet.biometric.CryptographyManager
import com.easy.wallet.databinding.FragmentTxPreviewBinding
import com.easy.wallet.ext.strByDecimal
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TxPreviewFragment : BaseBottomSheetFragment() {
    private val args: TxPreviewFragmentArgs by navArgs()

    private val binding by viewBinding(FragmentTxPreviewBinding::bind)

    private val cryptographyManager: CryptographyManager = CryptographyManager()
    private val viewModel by viewModel<TxPreviewViewModel> {
        parametersOf(args.currencyInfo)
    }

    override fun layout(): Int = R.layout.fragment_tx_preview

    @SuppressLint("SetTextI18n")
    override fun setupView() {
        super.setupView()

        binding.ivClose.onSingleClick(lifecycleScope) {
            findNavController().navigateUp()
        }

        binding.btnContinue.onSingleClick(lifecycleScope) {
            if (BiometricManager.from(requireContext().applicationContext)
                .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK) == BiometricManager.BIOMETRIC_SUCCESS
            ) {
                val cipher = cryptographyManager.getInitializedCipherForEncryption("test-name")
                BiometricPromptUtils.createBiometricPrompt(
                    requireActivity()
                ) {
                    binding.btnContinue.isEnabled = false
                    binding.btnContinue.showProgress {
                        buttonText = getString(R.string.text_send)
                        progressColor = Color.WHITE
                    }
                    viewModel.broadcastTransaction(
                        args.previewModel.rawData,
                        onSuccess = {
                            binding.btnContinue.isEnabled = true
                            binding.btnContinue.hideProgress("Continue")
                            findNavController().popBackStack(
                                findNavController().graph.startDestination,
                                false
                            )
                        },
                        onError = {
                            binding.btnContinue.isEnabled = true
                            binding.btnContinue.hideProgress("Send")
                        }
                    )
                }.authenticate(
                    BiometricPromptUtils.createPromptInfo(),
                    BiometricPrompt.CryptoObject(cipher)
                )
            }
        }

        with(args.previewModel) {
            binding.tvSendAmount.text = amount.strByDecimal()
            binding.tvSendSymbol.text = symbol
            binding.tvTo.text = to
            binding.tvFrom.text = from
            binding.tvFees.text = feeWithSymbol()
        }
    }
}
