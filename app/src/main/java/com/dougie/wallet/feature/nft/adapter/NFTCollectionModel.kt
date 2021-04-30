package com.dougie.wallet.feature.nft.adapter

import android.annotation.SuppressLint
import androidx.constraintlayout.widget.ConstraintLayout
import coil.load
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.dougie.framework.ext.onSingleClick
import com.dougie.wallet.R
import com.dougie.wallet.data.data.model.nft.NFTCollectionDataModel
import com.dougie.wallet.helper.KotlinEpoxyHolder
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.MainScope

@SuppressLint("NonConstantResourceId")
@EpoxyModelClass(layout = R.layout.rv_item_nft_collection)
abstract class NFTCollectionModel : EpoxyModelWithHolder<Holder>() {
    @EpoxyAttribute
    lateinit var infoData: NFTCollectionDataModel
    @EpoxyAttribute
    lateinit var onItemClick: () -> Unit

    private val scope = MainScope()

    override fun bind(holder: Holder) {
        holder.ivNFTCollectionIcon.load(infoData.imageUrl)
        holder.tvNFTCollectionName.text = infoData.name
        holder.tvNFTCollectionDesc.text = infoData.description
        holder.tvNFTCollectionAssetCount.text = infoData.ownedAssetCount.toString()

        holder.nftCollectionItem.onSingleClick(scope) {
            onItemClick.invoke()
        }
    }
}

class Holder : KotlinEpoxyHolder() {
    val nftCollectionItem by bind<ConstraintLayout>(R.id.nftCollectionItem)
    val ivNFTCollectionIcon by bind<ShapeableImageView>(R.id.ivNFTCollectionIcon)
    val tvNFTCollectionName by bind<MaterialTextView>(R.id.tvNFTCollectionName)
    val tvNFTCollectionDesc by bind<MaterialTextView>(R.id.tvNFTCollectionDesc)
    val tvNFTCollectionAssetCount by bind<MaterialTextView>(R.id.tvNFTCollectionAssetCount)
}
