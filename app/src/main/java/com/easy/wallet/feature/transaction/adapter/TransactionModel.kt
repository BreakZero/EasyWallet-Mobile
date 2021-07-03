package com.easy.wallet.feature.transaction.adapter

import android.annotation.SuppressLint
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.easy.wallet.R
import com.easy.wallet.data.data.model.TransactionDataModel
import com.easy.wallet.data.data.model.TxDirection
import com.easy.wallet.data.data.model.TxStatus
import com.easy.wallet.ext.downDecimal
import com.easy.wallet.helper.KotlinEpoxyHolder
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

@SuppressLint("NonConstantResourceId")
@EpoxyModelClass(layout = R.layout.rv_item_transaction_history)
abstract class TransactionModel : EpoxyModelWithHolder<Holder>() {
    @EpoxyAttribute
    lateinit var itemData: TransactionDataModel

    @EpoxyAttribute
    lateinit var onItemClick: () -> Unit

    @SuppressLint("SetTextI18n")
    override fun bind(holder: Holder) {
        holder.ivDirection.setImageResource(
            if (itemData.direction == TxDirection.SEND) R.drawable.ic_send
            else R.drawable.ic_receive
        )
        holder.tvAddress.text = itemData.recipient
        holder.tvStatus.text = when (itemData.status) {
            TxStatus.PENDING -> "Pending"
            else -> "Success"
        }
        holder.tvTime.text = itemData.time
        holder.tvAmount.text = "${
        if (itemData.direction == TxDirection.SEND) "-"
        else "+"
        }${itemData.amount.downDecimal(itemData.decimal, 8).toPlainString()} ${itemData.symbol}"
        holder.cardView.setOnClickListener {
            onItemClick.invoke()
        }
    }
}

class Holder : KotlinEpoxyHolder() {
    val ivDirection by bind<ShapeableImageView>(R.id.ivDirection)
    val tvAddress by bind<MaterialTextView>(R.id.tvAddress)
    val tvTime by bind<MaterialTextView>(R.id.tvTime)
    val tvAmount by bind<MaterialTextView>(R.id.tvAmount)
    val tvStatus by bind<MaterialTextView>(R.id.tvStatus)
    val cardView by bind<MaterialCardView>(R.id.txCardView)
}
