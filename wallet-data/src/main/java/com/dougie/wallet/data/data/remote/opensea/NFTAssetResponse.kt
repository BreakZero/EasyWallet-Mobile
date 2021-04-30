package com.dougie.wallet.data.data.remote.opensea

import com.squareup.moshi.Json

internal data class NFTAssetResponse(
    @field:Json(name = "assets")
    val assets: List<NFTAssetInfo>
)

internal data class NFTAssetInfo(
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
    @field:Json(name = "last_sale")
    val lastSale: LastSale,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "num_sales")
    val numSales: Int,
    @field:Json(name = "owner")
    val owner: Owner,
    @field:Json(name = "permalink")
    val permalink: String,
    @field:Json(name = "token_id")
    val tokenId: String
)

internal data class AssetContract(
    @field:Json(name = "address")
    val address: String,
    @field:Json(name = "asset_contract_type")
    val assetContractType: String,
    @field:Json(name = "buyer_fee_basis_points")
    val buyerFeeBasisPoints: Int,
    @field:Json(name = "created_date")
    val createdDate: String,
    @field:Json(name = "default_to_fiat")
    val defaultToFiat: Boolean,
    @field:Json(name = "description")
    val description: String?,
    @field:Json(name = "dev_buyer_fee_basis_points")
    val devBuyerFeeBasisPoints: Int,
    @field:Json(name = "dev_seller_fee_basis_points")
    val devSellerFeeBasisPoints: Int,
    @field:Json(name = "external_link")
    val externalLink: String?,
    @field:Json(name = "image_url")
    val imageUrl: String?,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "nft_version")
    val nftVersion: String?,
    @field:Json(name = "only_proxied_transfers")
    val onlyProxiedTransfers: Boolean,
    @field:Json(name = "opensea_buyer_fee_basis_points")
    val openseaBuyerFeeBasisPoints: Int,
    @field:Json(name = "opensea_seller_fee_basis_points")
    val openseaSellerFeeBasisPoints: Int,
    @field:Json(name = "opensea_version")
    val openseaVersion: String,
    @field:Json(name = "owner")
    val owner: Int,
    @field:Json(name = "payout_address")
    val payoutAddress: String?,
    @field:Json(name = "schema_name")
    val schemaName: String,
    @field:Json(name = "seller_fee_basis_points")
    val sellerFeeBasisPoints: Int,
    @field:Json(name = "symbol")
    val symbol: String,
    @field:Json(name = "total_supply")
    val totalSupply: String?
)

internal data class Asset(
    @field:Json(name = "decimals")
    val decimals: Int?,
    @field:Json(name = "token_id")
    val tokenId: String
)

internal data class Creator(
    @field:Json(name = "address")
    val address: String,
    @field:Json(name = "config")
    val config: String,
    @field:Json(name = "discord_id")
    val discordId: String,
    @field:Json(name = "profile_img_url")
    val profileImgUrl: String,
    @field:Json(name = "user")
    val user: User
)

internal data class DisplayData(
    @field:Json(name = "card_display_style")
    val cardDisplayStyle: String,
    @field:Json(name = "images")
    val images: List<String>
)

internal data class LastSale(
    @field:Json(name = "asset")
    val asset: Asset,
    @field:Json(name = "asset_bundle")
    val assetBundle: Any,
    @field:Json(name = "auction_type")
    val auctionType: Any,
    @field:Json(name = "created_date")
    val createdDate: String,
    @field:Json(name = "event_timestamp")
    val eventTimestamp: String,
    @field:Json(name = "event_type")
    val eventType: String,
    @field:Json(name = "payment_token")
    val paymentToken: PaymentToken,
    @field:Json(name = "quantity")
    val quantity: String,
    @field:Json(name = "total_price")
    val totalPrice: String
)

internal data class Owner(
    @field:Json(name = "address")
    val address: String,
    @field:Json(name = "config")
    val config: String,
    @field:Json(name = "discord_id")
    val discordId: String,
    @field:Json(name = "profile_img_url")
    val profileImgUrl: String,
    @field:Json(name = "user")
    val user: UserX
)

internal data class PaymentToken(
    @field:Json(name = "address")
    val address: String,
    @field:Json(name = "decimals")
    val decimals: Int,
    @field:Json(name = "eth_price")
    val ethPrice: String,
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "image_url")
    val imageUrl: String,
    @field:Json(name = "name")
    val name: String?,
    @field:Json(name = "symbol")
    val symbol: String,
    @field:Json(name = "usd_price")
    val usdPrice: String
)

internal data class User(
    @field:Json(name = "username")
    val username: String
)

internal data class UserX(
    @field:Json(name = "username")
    val username: String
)
