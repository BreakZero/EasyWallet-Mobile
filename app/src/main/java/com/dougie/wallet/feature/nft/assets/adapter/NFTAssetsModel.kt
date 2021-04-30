package com.dougie.wallet.feature.nft.assets.adapter

import android.annotation.SuppressLint
import coil.load
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.dougie.framework.ext.onSingleClick
import com.dougie.wallet.R
import com.dougie.wallet.data.data.model.nft.NFTAssetDataModel
import com.dougie.wallet.helper.KotlinEpoxyHolder
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.MainScope

@SuppressLint("NonConstantResourceId")
@EpoxyModelClass(layout = R.layout.rv_item_nft_asset)
abstract class NFTAssetModel : EpoxyModelWithHolder<Holder>() {
    @EpoxyAttribute
    lateinit var infoData: NFTAssetDataModel

    @EpoxyAttribute
    lateinit var onItemClick: () -> Unit

    private val scope = MainScope()

    override fun bind(holder: Holder) {
        holder.ivNFTAssetImage.load(infoData.imagePreviewUrl) {
            allowHardware(false)
        }
        holder.tvNFTAssetName.text = infoData.name
        holder.tvNFTAssetDesc.text = infoData.description

        holder.nftAssetCard.onSingleClick(scope) {
            onItemClick.invoke()
        }
    }
}

class Holder : KotlinEpoxyHolder() {
    val nftAssetCard by bind<MaterialCardView>(R.id.nftAssetCard)
    val ivNFTAssetImage by bind<ShapeableImageView>(R.id.ivNFTAssetImage)
    val tvNFTAssetName by bind<MaterialTextView>(R.id.tvNFTAssetName)
    val tvNFTAssetDesc by bind<MaterialTextView>(R.id.tvNFTAssetDesc)
}
