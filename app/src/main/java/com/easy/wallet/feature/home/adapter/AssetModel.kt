package com.easy.wallet.feature.home.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.easy.framework.ext.onSingleClick
import com.easy.wallet.R
import com.easy.wallet.data.Asset
import com.easy.wallet.helper.KotlinEpoxyHolder
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

@SuppressLint("NonConstantResourceId")
@EpoxyModelClass(layout = R.layout.rv_item_home_asset)
abstract class AssetModel : EpoxyModelWithHolder<Holder>() {
  @EpoxyAttribute
  lateinit var assetData: Asset

  @EpoxyAttribute
  lateinit var onItemClick: () -> Unit

  @EpoxyAttribute
  lateinit var onReceive: (String) -> Unit

  private val scope = MainScope()

  override fun bind(holder: Holder) {
    holder.assetCard.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor(assetData.coinInfo.accentColor)))
    holder.assetCard.onSingleClick(scope) {
      onItemClick.invoke()
    }
    holder.tvAddress.text = assetData.provider.getAddress(false)
    holder.tvCoinName.text = assetData.coinInfo.name
    holder.tvBalance.text = assetData.balance?.let {
      "${it.toPlainString()} ${assetData.coinInfo.symbol}"
    } ?: "loading"
    holder.tvAddress.onSingleClick(scope) {
      onReceive.invoke(assetData.provider.getAddress(false))
    }
  }

  override fun unbind(holder: Holder) {
    scope.cancel()
    super.unbind(holder)
  }
}

class Holder : KotlinEpoxyHolder() {
  val assetCard by bind<MaterialCardView>(R.id.assetCard)
  val tvCoinName by bind<MaterialTextView>(R.id.tvCoinName)
  val tvAddress by bind<MaterialTextView>(R.id.tvAddress)
  val tvBalance by bind<MaterialTextView>(R.id.tvBalance)
}
