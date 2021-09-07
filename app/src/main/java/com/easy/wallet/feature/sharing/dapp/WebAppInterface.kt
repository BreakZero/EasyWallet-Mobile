package com.easy.wallet.feature.sharing.dapp

import android.content.Context
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.easy.wallet.ext.sendError
import com.easy.wallet.ext.sendResult
import org.json.JSONObject
import splitties.alertdialog.appcompat.cancelButton
import splitties.alertdialog.appcompat.message
import splitties.alertdialog.appcompat.okButton
import splitties.alertdialog.appcompat.title
import splitties.alertdialog.material.materialAlertDialog

class WebAppInterface(
  private val context: Context,
  private val webView: WebView,
  private val dappUrl: String
) {

  val addr = "0x81080a7e991bcdddba8c2302a70f45d6bd369ab5"

  @JavascriptInterface
  fun postMessage(json: String) {

    val obj = JSONObject(json)
    println(obj)
    val id = obj.getLong("id")

    when (DAppMethod.fromValue(obj.getString("name"))) {
      DAppMethod.REQUESTACCOUNTS -> {
        context.materialAlertDialog {
          title = "Request Accounts"
          message = "DApp(${dappUrl}) need to get your address"
          okButton {
            val setAddress = "window.ethereum.setAddress(\"$addr\");"
            val callback = "window.ethereum.sendResponse($id, [\"$addr\"])"
            webView.post {
              webView.evaluateJavascript(setAddress) {
                // ignore
              }
              webView.evaluateJavascript(callback) { value ->
                println(value)
              }
            }
          }
          cancelButton()
        }.show()
      }
      DAppMethod.SIGNMESSAGE -> {
        val data = extractMessage(obj)
        handleSignMessage(id = id, data = data, addPrefix = false)
      }
      else -> {
      }
      // handle other methods here
      // signTransaction, signMessage, ecRecover, watchAsset, addEthereumChain
    }
  }

  private fun extractMessage(json: JSONObject): String {
    val param = json.getJSONObject("object")
    return param.getString("data")
  }

  private fun handleSignMessage(id: Long, data: String, addPrefix: Boolean) {
    context.materialAlertDialog {
      title = "Sign Message"
      message = if (addPrefix) "0x$data" else data
      cancelButton {
        webView.sendError("Cancel", id)
      }
      okButton {
        webView.sendResult("", id)
      }
    }.show()
  }
}
