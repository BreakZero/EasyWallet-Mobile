package com.easy.wallet.feature.sharing.dapp

import android.content.Context
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import com.easy.wallet.data.etx.toHex
import com.easy.wallet.data.etx.toHexByteArray
import com.easy.wallet.ext.sendError
import com.easy.wallet.ext.sendResult
import org.json.JSONObject
import org.web3j.utils.Numeric
import splitties.alertdialog.appcompat.cancelButton
import splitties.alertdialog.appcompat.message
import splitties.alertdialog.appcompat.okButton
import splitties.alertdialog.appcompat.title
import splitties.alertdialog.material.materialAlertDialog
import timber.log.Timber
import wallet.core.jni.CoinType
import wallet.core.jni.Curve
import wallet.core.jni.PrivateKey

class WebAppInterface(
  private val context: Context,
  private val webView: WebView,
  private val dappUrl: String
) {
  private val privateKey =
    PrivateKey("0x4646464646464646464646464646464646464646464646464646464646464646".toHexByteArray())
  private val addr =
    if (dappUrl == "https://js-eth-sign.surge.sh") CoinType.ETHEREUM.deriveAddress(privateKey)
      .lowercase()
    else "0x81080a7e991bcdddba8c2302a70f45d6bd369ab5"

  @JavascriptInterface
  fun postMessage(json: String) {
    Timber.d("============ $json")
    val obj = JSONObject(json)
    val id = obj.getLong("id")
    when (val method = DAppMethod.fromValue(obj.getString("name"))) {
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
      DAppMethod.SIGNTRANSACTION -> {
        val data = extractMessage(obj)
        Toast.makeText(context, data.toHex(), Toast.LENGTH_SHORT).show()
      }
      DAppMethod.SIGNMESSAGE -> {
        val data = extractMessage(obj)
        handleSignMessage(id, data, addPrefix = false)
      }
      DAppMethod.SIGNPERSONALMESSAGE -> {
        val data = extractMessage(obj)
        handleSignMessage(id, data, addPrefix = true)
      }
      DAppMethod.SIGNTYPEDMESSAGE -> {
        val data = extractMessage(obj)
        val raw = extractRaw(obj)
        handleSignTypedMessage(id, data, raw)
      }
      else -> {
        context.materialAlertDialog {
          title = "Error"
          message = "$method not implemented"
          okButton {
          }
        }.show()
      }
    }
  }

  private fun extractMessage(json: JSONObject): ByteArray {
    val param = json.getJSONObject("object")
    val data = param.getString("data")
    return Numeric.hexStringToByteArray(data)
  }

  private fun extractRaw(json: JSONObject): String {
    val param = json.getJSONObject("object")
    return param.getString("raw")
  }

  private fun handleSignMessage(id: Long, data: ByteArray, addPrefix: Boolean) {
    context.materialAlertDialog {
      title = "Sign Message"
      message = if (addPrefix) String(data, Charsets.UTF_8) else Numeric.toHexString(data)
      cancelButton {
        webView.sendError("Cancel", id)
      }
      okButton {
        webView.sendResult(signEthereumMessage(data, addPrefix), id)
      }
    }.show()
  }

  private fun handleSignTypedMessage(id: Long, data: ByteArray, raw: String) {
    context.materialAlertDialog {
      title = "Sign Typed Message"
      message = raw
      cancelButton {
        webView.sendError("Cancel", id)
      }
      okButton {
        webView.sendResult(signEthereumMessage(data, false), id)
      }
    }.show()
  }

  private fun signEthereumMessage(message: ByteArray, addPrefix: Boolean): String {
    var data = message
    if (addPrefix) {
      val messagePrefix = "\u0019Ethereum Signed Message:\n"
      val prefix = (messagePrefix + message.size).toByteArray()
      val result = ByteArray(prefix.size + message.size)
      System.arraycopy(prefix, 0, result, 0, prefix.size)
      System.arraycopy(message, 0, result, prefix.size, message.size)
      data = wallet.core.jni.Hash.keccak256(result)
    }

    val signatureData = privateKey.sign(data, Curve.SECP256K1)
      .apply {
        (this[this.size - 1]) = (this[this.size - 1] + 27).toByte()
      }
    return Numeric.toHexString(signatureData)
  }
}
