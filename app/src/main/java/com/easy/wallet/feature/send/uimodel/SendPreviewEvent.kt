package com.easy.wallet.feature.send.uimodel

import io.uniflow.core.flow.data.UIEvent

sealed class SendPreviewEvent : UIEvent() {
  object EventBroadcast : SendPreviewEvent()
}