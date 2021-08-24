package com.easy.wallet.data.data.remote.opensea

import com.squareup.moshi.Json

internal data class NFTSingleAssetResponse(
  @field:Json(name = "animation_original_url")
  val animationOriginalUrl: String?,
  @field:Json(name = "animation_url")
  val animationUrl: String?,
  @field:Json(name = "asset_contract")
  val assetContract: AssetContract,
  @field:Json(name = "background_color")
  val backgroundColor: String?,
  @field:Json(name = "collection")
  val collection: NFTCollectionInfo,
  @field:Json(name = "creator")
  val creator: Creator,
  @field:Json(name = "traits")
  val traits: List<NFTTrait>,
  @field:Json(name = "decimals")
  val decimals: Int?,
  @field:Json(name = "description")
  val description: String?,
  @field:Json(name = "external_link")
  val externalLink: String?,
  @field:Json(name = "id")
  val id: Int,
  @field:Json(name = "image_original_url")
  val imageOriginalUrl: String,
  @field:Json(name = "image_preview_url")
  val imagePreviewUrl: String,
  @field:Json(name = "image_thumbnail_url")
  val imageThumbnailUrl: String,
  @field:Json(name = "image_url")
  val imageUrl: String,
  @field:Json(name = "is_presale")
  val isPresale: Boolean,
  @field:Json(name = "name")
  val name: String,
  @field:Json(name = "num_sales")
  val numSales: Int,
  @field:Json(name = "owner")
  val owner: Owner,
  @field:Json(name = "ownership")
  val ownership: Ownership,
  @field:Json(name = "permalink")
  val permalink: String,
  @field:Json(name = "supports_wyvern")
  val supportsWyvern: Boolean,
  @field:Json(name = "token_id")
  val tokenId: String
)

internal data class Ownership(
  @field:Json(name = "owner")
  val owner: Owner,
  @field:Json(name = "quantity")
  val quantity: String
)
