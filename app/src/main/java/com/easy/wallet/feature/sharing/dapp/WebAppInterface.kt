package com.easy.wallet.feature.sharing.dapp

import android.webkit.JavascriptInterface
import android.webkit.WebView
import org.json.JSONObject
import timber.log.Timber

class WebAppInterface(private val context: WebView) {
  @JavascriptInterface
  fun postMessage(json: String) {
    Timber.d("======= $json")
    val obj = JSONObject(json)
    val id = obj["id"]
    val addr = "0x81080a7e991bcdddba8c2302a70f45d6bd369ab5"

    when(obj["name"]) {
      "requestAccounts" -> {
        val callback = "window.ethereum.sendResponse($id, [\"$addr\"])"
        context.post {
          context.evaluateJavascript(callback) { value ->
            Timber.d("======= $value")
          }
        }
      }
      // handle other methods here
      // signTransaction, signMessage, ecRecover, watchAsset, addEthereumChain
    }
  }
}
