package com.easy.wallet.feature.home.uimodel

import com.easy.wallet.data.Asset
import io.uniflow.core.flow.data.UIState

data class AssetListState(
    val assets: List<Asset>
) : UIState()
