package com.easy.wallet.feature.sharing.dapp

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import com.easy.framework.base.BaseFragment
import com.easy.framework.delegate.viewBinding
import com.easy.wallet.R
import com.easy.wallet.data.WalletDataSDK
import com.easy.wallet.databinding.FragmentWebviewBinding
import com.google.android.material.appbar.MaterialToolbar

class DAppBrowserFragment : BaseFragment(R.layout.fragment_webview) {
  override fun ownerToolbar(): MaterialToolbar? = null

  private val binding by viewBinding(FragmentWebviewBinding::bind)

  @SuppressLint("SetJavaScriptEnabled")
  override fun setupView() {
    super.setupView()
    val provderJs = loadProviderJs()
    val initJs = loadInitJs(
      WalletDataSDK.chainId().id,
      WalletDataSDK.dAppRPC()
    )
    println("file lenght: ${provderJs.length}")
    WebView.setWebContentsDebuggingEnabled(true)
    binding.webView.settings.javaScriptEnabled = true
    binding.webView.addJavascriptInterface(WebAppInterface(binding.webView), "_tw_")

    val webViewClient = object : WebViewClient() {
      override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        view?.evaluateJavascript(provderJs, null)
        view?.evaluateJavascript(initJs, null)
      }
    }
    binding.webView.webViewClient = webViewClient
    binding.webView.loadUrl("https://app.uniswap.org/#/swap")
  }

  private fun loadProviderJs() = resources.openRawResource(R.raw.trust_min).bufferedReader().use {
    it.readText()
  }

  private fun loadInitJs(chainId: Int, rpcUrl: String): String {
    val source = """
        (function() {
            var config = {
                chainId: $chainId,
                rpcUrl: "$rpcUrl",
                isDebug: true
            };
            window.ethereum = new trustwallet.Provider(config);
            window.web3 = new trustwallet.Web3(window.ethereum);
            trustwallet.postMessage = (json) => {
                window._tw_.postMessage(JSON.stringify(json));
            }
        })();
        """
    return source
  }
}