package com.easy.wallet.feature.defi.uimodels

import io.uniflow.core.flow.data.UIState

data class DeFiListState(
  val dapps: List<DAppInfo>
) : UIState()

data class DAppInfo(
  val icon: String,
  val name: String,
  val link: String
)
