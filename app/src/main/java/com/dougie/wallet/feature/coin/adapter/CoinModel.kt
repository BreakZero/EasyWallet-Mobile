package com.dougie.wallet.feature.coin.adapter

import android.annotation.SuppressLint
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.dougie.wallet.R
import com.dougie.wallet.helper.KotlinEpoxyHolder
import com.google.android.material.checkbox.MaterialCheckBox

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
        holder.chbItemCoin.text = coinName
        holder.chbItemCoin.isChecked = check

        holder.chbItemCoin.setOnCheckedChangeListener { buttonView, _ ->
            if (buttonView.isPressed) onCheckChanged.invoke()
        }
    }
}

class Holder : KotlinEpoxyHolder() {
    val chbItemCoin by bind<MaterialCheckBox>(R.id.chbItemCoin)
}
