package com.easy.wallet.feature.send.uimodel

import io.uniflow.core.flow.data.UIEvent

sealed class SendUIEvents : UIEvent() {
    object ToSend : SendUIEvents()
    object BuildError: SendUIEvents()
}
