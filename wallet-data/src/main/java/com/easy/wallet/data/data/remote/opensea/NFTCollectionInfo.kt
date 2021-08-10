package com.easy.wallet.data.data.remote.opensea

import com.squareup.moshi.Json

internal data class NFTCollectionInfo(
  @field:Json(name = "primary_asset_contracts")
  val assetContracts: List<AssetContract>,
  @field:Json(name = "banner_image_url")
  val bannerImageUrl: String?,
  @field:Json(name = "chat_url")
  val chatUrl: String?,
  @field:Json(name = "created_date")
  val createdDate: String,
  @field:Json(name = "default_to_fiat")
  val defaultToFiat: Boolean,
  @field:Json(name = "description")
  val description: String,
  @field:Json(name = "dev_buyer_fee_basis_points")
  val devBuyerFeeBasisPoints: String,
  @field:Json(name = "dev_seller_fee_basis_points")
  val devSellerFeeBasisPoints: String,
  @field:Json(name = "discord_url")
  val discordUrl: String?,
  @field:Json(name = "external_url")
  val externalUrl: String?,
  @field:Json(name = "featured")
  val featured: Boolean,
  @field:Json(name = "featured_image_url")
  val featuredImageUrl: String?,
  @field:Json(name = "hidden")
  val hidden: Boolean,
  @field:Json(name = "image_url")
  val imageUrl: String,
  @field:Json(name = "instagram_username")
  val instagramUsername: String?,
  @field:Json(name = "is_subject_to_whitelist")
  val isSubjectToWhitelist: Boolean,
  @field:Json(name = "large_image_url")
  val largeImageUrl: String?,
  @field:Json(name = "medium_username")
  val mediumUsername: String?,
  @field:Json(name = "name")
  val name: String,
  @field:Json(name = "only_proxied_transfers")
  val onlyProxiedTransfers: Boolean,
  @field:Json(name = "opensea_buyer_fee_basis_points")
  val openseaBuyerFeeBasisPoints: String,
  @field:Json(name = "opensea_seller_fee_basis_points")
  val openseaSellerFeeBasisPoints: String,
  @field:Json(name = "owned_asset_count")
  val ownedAssetCount: Int,
  @field:Json(name = "payout_address")
  val payoutAddress: String?,
  @field:Json(name = "require_email")
  val requireEmail: Boolean,
  @field:Json(name = "safelist_request_status")
  val safelistRequestStatus: String,
  @field:Json(name = "short_description")
  val shortDescription: String?,
  @field:Json(name = "slug")
  val slug: String,
  @field:Json(name = "telegram_url")
  val telegramUrl: String?,
  @field:Json(name = "twitter_username")
  val twitterUsername: String?,
  @field:Json(name = "wiki_url")
  val wikiUrl: String?
)
