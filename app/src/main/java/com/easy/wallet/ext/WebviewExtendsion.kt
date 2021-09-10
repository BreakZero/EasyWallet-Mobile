package com.easy.wallet.ext

import android.webkit.WebView

fun WebView.sendError(message: String, methodId: Long) {
  val script = "window.ethereum.sendError($methodId, \"$message\")"
  this.post {
    this.evaluateJavascript(script) {}
  }
}

fun WebView.sendResult(message: String, methodId: Long) {
  val script = "window.ethereum.sendResponse($methodId, \"$message\")"
  this.post {
    this.evaluateJavascript(script) {}
  }
}

fun WebView.sendResults(messages: List<String>, methodId: Long) {
  val message = messages.joinToString(separator = ",")
  val script = "window.ethereum.sendResponse($methodId, \"$message\")"
  this.post {
    this.evaluateJavascript(script) {}
  }
}