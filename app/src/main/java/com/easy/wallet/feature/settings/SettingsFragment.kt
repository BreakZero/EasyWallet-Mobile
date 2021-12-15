package com.easy.wallet.feature.settings

import android.content.Intent
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import com.easy.framework.base.BaseFragment
import com.easy.framework.delegate.viewBinding
import com.easy.framework.ext.onSingleClick
import com.easy.wallet.R
import com.easy.wallet.biometric.BiometricPromptUtils
import com.easy.wallet.biometric.CryptographyManager
import com.easy.wallet.databinding.FragmentSettingsBinding
import com.easy.wallet.ext.start
import com.easy.wallet.feature.settings.uimodel.SettingsState
import com.easy.wallet.feature.sharing.ScannerFragment
import com.easy.wallet.feature.wallectconnet.WalletConnectService
import com.google.android.material.appbar.MaterialToolbar
import io.uniflow.android.livedata.onStates
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {
  private val binding by viewBinding(FragmentSettingsBinding::bind)
  private val viewModel by viewModel<SettingsViewModel>()

  private val cryptographyManager: CryptographyManager = CryptographyManager()

  override fun ownerToolbar(): MaterialToolbar? = null

  override fun setupView() {
    super.setupView()
    setTitle(getString(R.string.settings))

    onStates(viewModel) {
      when (it) {
        is SettingsState -> binding.tvChainNetwork.text = it.currChain
      }
    }

    binding.tvAbout.onSingleClick(lifecycleScope) {
      start(R.id.action_to_testing)
    }
    binding.flChainNetwork.onSingleClick(lifecycleScope) {
      start(R.id.action_home_to_settings)
    }
    binding.tvPreferences.onSingleClick(lifecycleScope) {
    }

    binding.swtEnableBiometric.setOnCheckedChangeListener { view, _ ->
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
