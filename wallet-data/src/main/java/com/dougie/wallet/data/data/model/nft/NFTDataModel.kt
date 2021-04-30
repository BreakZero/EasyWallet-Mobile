package com.dougie.wallet.data.data.model.nft

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NFTAssetDataModel(
    val tokenId: String,
    val name: String,
    val description: String? = null,
    val contractAddress: String,
    val imagePreviewUrl: String,
    val imageUrl: String? = null,
    val animationUrl: String? = null,
    val quantity: Int,
    val nftType: NFTType,
    val permalink: String,
    val attrs: List<NTFAttr> = emptyList()
) : Parcelable {
    companion object {
        val EMPTY = NFTAssetDataModel(
            tokenId = "",
            name = "",
            contractAddress = "",
            imagePreviewUrl = "",
            quantity = 0,
            nftType = NFTType.ERC1155,
            permalink = ""
        )
    }

    fun isEmpty(): Boolean {
        return tokenId.isEmpty()
    }
}

@Parcelize
data class NTFAttr(
    val type: String,
    val value: String
) : Parcelable

enum class NFTType(schema: String) {
    ERC721("ERC721"), ERC1155("ERC1155")
}
