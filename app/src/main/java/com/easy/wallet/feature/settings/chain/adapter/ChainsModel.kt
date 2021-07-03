package com.easy.wallet.feature.settings.chain.adapter

import android.annotation.SuppressLint
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.easy.wallet.R
import com.easy.wallet.helper.KotlinEpoxyHolder
import com.google.android.material.radiobutton.MaterialRadioButton

@SuppressLint("NonConstantResourceId")
@EpoxyModelClass(layout = R.layout.rv_item_chain)
abstract class ChainsModel : EpoxyModelWithHolder<Holder>() {
    @EpoxyAttribute
    lateinit var item: WrapChain

    @EpoxyAttribute
    var check: Boolean = false

    @EpoxyAttribute
    lateinit var onCheckChanged: () -> Unit

    override fun bind(holder: Holder) {
        holder.chainRadioButton.text = item.name
        holder.chainRadioButton.isChecked = item.checked

        holder.chainRadioButton.setOnCheckedChangeListener { buttonView, _ ->
            if (buttonView.isPressed) onCheckChanged.invoke()
        }
    }
}

class Holder : KotlinEpoxyHolder() {
    val chainRadioButton by bind<MaterialRadioButton>(R.id.chainRadioButton)
}

data class WrapChain(
    val name: String,
    val checked: Boolean
)
