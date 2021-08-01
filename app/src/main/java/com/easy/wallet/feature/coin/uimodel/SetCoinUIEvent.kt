package com.easy.wallet.feature.coin.uimodel

import io.uniflow.core.flow.data.UIEvent

sealed class SetCoinUIEvent : UIEvent() {
    object UpdateState : SetCoinUIEvent()
    data class DoneEvent(val result: Boolean) : SetCoinUIEvent()
}
