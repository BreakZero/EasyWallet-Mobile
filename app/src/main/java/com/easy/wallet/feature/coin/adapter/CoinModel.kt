package com.easy.wallet.feature.coin.adapter

import android.annotation.SuppressLint
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.easy.wallet.R
import com.easy.wallet.helper.KotlinEpoxyHolder
import com.google.android.material.switchmaterial.SwitchMaterial

@SuppressLint("NonConstantResourceId")
@EpoxyModelClass(layout = R.layout.rv_item_coin_list)
abstract class CoinModel : EpoxyModelWithHolder<Holder>() {
  @EpoxyAttribute
  lateinit var coinName: String

  @EpoxyAttribute
  var check: Boolean = false

  @EpoxyAttribute
  lateinit var onCheckChanged: () -> Unit

  override fun bind(holder: Holder) {
    holder.swtToggle.text = coinName
    holder.swtToggle.isChecked = check

    holder.swtToggle.setOnCheckedChangeListener { buttonView, _ ->
      if (buttonView.isPressed) onCheckChanged.invoke()
    }
  }
}

class Holder : KotlinEpoxyHolder() {
  val swtToggle by bind<SwitchMaterial>(R.id.toggleItem)
}
