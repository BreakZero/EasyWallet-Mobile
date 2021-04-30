package com.dougie.wallet.feature.settings

import android.Manifest
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dougie.framework.base.BaseFragment
import com.dougie.framework.delegate.viewBinding
import com.dougie.framework.ext.onSingleClick
import com.dougie.wallet.R
import com.dougie.wallet.biometric.BiometricPromptUtils
import com.dougie.wallet.biometric.CryptographyManager
import com.dougie.wallet.databinding.FragmentSettingsBinding
import com.dougie.wallet.feature.sharing.ScannerFragment
import com.dougie.wallet.feature.wallectconnet.WalletConnectService
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension
import timber.log.Timber

@KoinApiExtension
class SettingsFragment : BaseFragment(R.layout.fragment_settings) {
    private val binding by viewBinding(FragmentSettingsBinding::bind)
    private val viewModel by viewModel<SettingsViewModel>()

    private val cryptographyManager: CryptographyManager = CryptographyManager()

    private val askFilePermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val isGrandAll = permissions.entries.map {
                it.value
            }.none {
                it.not()
            }
            if (isGrandAll) {
                findNavController().navigate(R.id.global_action_scan)
            } else {
                Timber.d("file store permission denied")
            }
        }

    override fun ownerToolbar(): MaterialToolbar? = null

    override fun setupView() {
        super.setupView()
        setTitle(getString(R.string.settings))

        viewModel.initState {
            binding.tvChainNetwork.text = it
        }

        binding.tvAbout.onSingleClick(lifecycleScope) {
            Timber.d("Hello world")
        }
        binding.flChainNetwork.onSingleClick(lifecycleScope) {
            findNavController().navigate(R.id.action_home_to_settings)
        }

        binding.tvUniSwap.onSingleClick(lifecycleScope) {
            askFilePermission.launch(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
        }

        binding.swtEnableBiometric.setOnCheckedChangeListener { view, isChecked ->
            if (!view.isPressed) {
                return@setOnCheckedChangeListener
            }
            if (BiometricManager.from(requireContext().applicationContext)
                .canAuthenticate(Authenticators.BIOMETRIC_WEAK) == BiometricManager.BIOMETRIC_SUCCESS
            ) {
                val cipher = cryptographyManager.getInitializedCipherForEncryption("test-name")
                BiometricPromptUtils.createBiometricPrompt(
                    requireActivity(),
                    onError = {
                        binding.swtEnableBiometric.toggle()
                    }
                ).authenticate(
                    BiometricPromptUtils.createPromptInfo(),
                    BiometricPrompt.CryptoObject(cipher)
                )
            }
        }

        setFragmentResultListener(ScannerFragment.REQUEST_QR_CODE) { _, bundle ->
            val result = bundle.getString(ScannerFragment.KEY_QR_CODE)
            Timber.d(result)
            Intent(requireContext(), WalletConnectService::class.java).also { intent ->
                intent.putExtra(WalletConnectService.EXTRA_KEY_WC_URI, result)
                requireActivity().startService(intent)
            }
        }
    }
}
