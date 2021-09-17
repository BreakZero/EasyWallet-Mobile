package com.easy.wallet.feature.sharing

import android.annotation.SuppressLint
import com.easy.framework.base.BaseFragment
import com.easy.framework.delegate.viewBinding
import com.easy.wallet.R
import com.easy.wallet.databinding.FragmentWebviewBinding
import com.google.android.material.appbar.MaterialToolbar

class BrowserFragment : BaseFragment(R.layout.fragment_webview) {
  override fun ownerToolbar(): MaterialToolbar? = null

  private val binding by viewBinding(FragmentWebviewBinding::bind)

  @SuppressLint("SetJavaScriptEnabled")
  override fun setupView() {
    super.setupView()
    binding.webView.run {
      settings.javaScriptEnabled = true
      settings.domStorageEnabled = true
    }

    binding.webView.loadUrl("https://pancakeswap.finance/swap")
  }
}
