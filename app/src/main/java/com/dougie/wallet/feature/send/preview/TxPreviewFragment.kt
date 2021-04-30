package com.dougie.wallet.feature.send.preview

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dougie.framework.base.BaseBottomSheetFragment
import com.dougie.framework.delegate.viewBinding
import com.dougie.framework.ext.onSingleClick
import com.dougie.wallet.R
import com.dougie.wallet.biometric.BiometricPromptUtils
import com.dougie.wallet.biometric.CryptographyManager
import com.dougie.wallet.databinding.FragmentTxPreviewBinding
import com.dougie.wallet.ext.strByDecimal
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import org.koin.core.scope.KoinScopeComponent
import org.koin.core.scope.Scope
import org.koin.core.scope.inject
import org.koin.core.scope.newScope
import timber.log.Timber

class TxPreviewFragment : BaseBottomSheetFragment(), KoinScopeComponent {

    override val scope: Scope by lazy { newScope(this) }

    private val args: TxPreviewFragmentArgs by navArgs()

    private val binding by viewBinding(FragmentTxPreviewBinding::bind)

    private val cryptographyManager: CryptographyManager = CryptographyManager()
    private val viewModel by inject<TxPreviewViewModel>()

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
                        buttonText = "sending"
                        progressColor = Color.WHITE
                    }
                    viewModel.broadcastTransaction(args.previewModel.rawData) {
                        Timber.d("===$it")
                        binding.btnContinue.isEnabled = true
                        binding.btnContinue.hideProgress("Continue")
                        findNavController().popBackStack(
                            findNavController().graph.startDestination,
                            false
                        )
                    }
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

    override fun onDestroy() {
        super.onDestroy()
        closeScope()
    }
}
