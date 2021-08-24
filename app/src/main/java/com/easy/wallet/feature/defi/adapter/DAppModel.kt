package com.easy.wallet.feature.defi.adapter

import android.annotation.SuppressLint
import androidx.constraintlayout.widget.ConstraintLayout
import coil.load
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.easy.framework.ext.onSingleClick
import com.easy.wallet.R
import com.easy.wallet.feature.defi.uimodels.DAppInfo
import com.easy.wallet.helper.KotlinEpoxyHolder
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.GlobalScope

@SuppressLint("NonConstantResourceId")
@EpoxyModelClass(layout = R.layout.rv_item_dapp)
abstract class DAppModel : EpoxyModelWithHolder<Holder>() {
  @EpoxyAttribute
  lateinit var dAppInfo: DAppInfo

  @EpoxyAttribute
  lateinit var toDApp: (link: String) -> Unit

  override fun bind(holder: Holder) {
    holder.icon.load(dAppInfo.icon) {
      crossfade(true)
    }
    holder.name.text = dAppInfo.name

    holder.rootLayout.onSingleClick(GlobalScope) {
      toDApp.invoke(dAppInfo.link)
    }
  }
}

class Holder : KotlinEpoxyHolder() {
  val rootLayout by bind<ConstraintLayout>(R.id.rootLayout)
  val icon by bind<ShapeableImageView>(R.id.ivDAppIcon)
  val name by bind<MaterialTextView>(R.id.tvDAppName)
}
