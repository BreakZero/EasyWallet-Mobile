package com.easy.wallet.feature.sharing.dapp

import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import org.json.JSONObject
import timber.log.Timber

class WebAppInterface(private val context: WebView) {
  @JavascriptInterface
  fun postMessage(json: String) {
    Timber.d("============ $json")
    val obj = JSONObject(json)
    val id = obj["id"]
    val addr = "0x81080a7e991bcdddba8c2302a70f45d6bd369ab5"
    val method = DAppMethod.fromValue(obj.getString("name"))
    when (method) {
      DAppMethod.REQUESTACCOUNTS -> {
        val setAddress = "window.ethereum.setAddress(\"$addr\");"
        val callback = "window.ethereum.sendResponse($id, [\"$addr\"])"
        context.post {
          context.evaluateJavascript(setAddress) {
            // ignore
          }
          context.evaluateJavascript(callback) { value ->
            println(value)
          }
        }
      }
      DAppMethod.SIGNTRANSACTION -> {
        val data = extractMessage(obj)
        Toast.makeText(context.context, data, Toast.LENGTH_SHORT).show()
      }
      DAppMethod.SIGNTYPEDMESSAGE -> {
        val data = extractMessage(obj)
        Toast.makeText(context.context, data, Toast.LENGTH_SHORT).show()
      }
      // handle other methods here
      // signTransaction, signMessage, ecRecover, watchAsset, addEthereumChain
    }
  }
  private fun extractMessage(json: JSONObject): String {
    val param = json.getJSONObject("object")
    val data = param.getString("data")
    return data
  }
}
