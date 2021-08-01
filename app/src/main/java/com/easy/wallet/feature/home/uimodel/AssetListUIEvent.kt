package com.easy.wallet.feature.home.uimodel

import io.uniflow.core.flow.data.UIEvent

sealed class AssetListUIEvent : UIEvent() {
    object RefreshEvent : AssetListUIEvent()
}
