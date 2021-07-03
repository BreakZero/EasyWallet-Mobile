package com.easy.wallet.data.data.model.nft

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
class NFTCollectionDataModel(
    val slug: String,
    val name: String,
    val contractAddress: String,
    val imageUrl: String,
    val description: String,
    val ownedAssetCount: Int
) : Parcelable
