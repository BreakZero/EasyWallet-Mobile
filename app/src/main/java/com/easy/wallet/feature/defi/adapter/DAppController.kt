package com.easy.wallet.feature.defi.adapter

import com.airbnb.epoxy.TypedEpoxyController
import com.easy.wallet.feature.defi.uimodels.DAppInfo

class DAppController(private val onChecked: (String) -> Unit) :
  TypedEpoxyController<List<DAppInfo>>() {
  override fun buildModels(data: List<DAppInfo>?) {
    data?.forEach { entity ->
      dApp {
        id(entity.hashCode())
        dAppInfo(entity)
        toDApp {
          onChecked.invoke(it)
        }
      }
    }
  }
}
