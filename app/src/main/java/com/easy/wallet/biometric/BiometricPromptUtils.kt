package com.easy.wallet.biometric

import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import timber.log.Timber

object BiometricPromptUtils {
  fun createBiometricPrompt(
    activity: FragmentActivity,
    onError: (errorCode: Int) -> Unit = {},
    processSuccess: (BiometricPrompt.AuthenticationResult) -> Unit = {}
  ): BiometricPrompt {
    val executor = ContextCompat.getMainExecutor(activity)

    val callback = object : BiometricPrompt.AuthenticationCallback() {

      override fun onAuthenticationError(errCode: Int, errString: CharSequence) {
        super.onAuthenticationError(errCode, errString)
        Timber.d("errCode is $errCode and errString is: $errString")
        onError.invoke(errCode)
      }

      override fun onAuthenticationFailed() {
        super.onAuthenticationFailed()
        Timber.d("Biometric authentication failed for unknown reason.")
        onError.invoke(-1)
      }

      override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        super.onAuthenticationSucceeded(result)
        Timber.d("Authentication was successful")
        processSuccess(result)
      }
    }
    return BiometricPrompt(activity, executor, callback)
  }

  fun createPromptInfo(
    title: String = "Set up",
    negativeText: String = "Cancel"
  ): BiometricPrompt.PromptInfo =
    BiometricPrompt.PromptInfo.Builder().apply {
      setTitle(title)
      setNegativeButtonText(negativeText)
      setConfirmationRequired(false)
    }.build()
}
