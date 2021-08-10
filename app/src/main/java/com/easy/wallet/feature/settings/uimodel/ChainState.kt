package com.easy.wallet.feature.settings.uimodel

import com.easy.wallet.feature.settings.chain.adapter.WrapChain
import io.uniflow.core.flow.data.UIState

data class ChainState(
  val list: List<WrapChain>
) : UIState()
