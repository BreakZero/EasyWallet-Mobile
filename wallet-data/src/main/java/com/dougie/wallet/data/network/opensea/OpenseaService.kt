package com.dougie.wallet.data.network.opensea

import com.dougie.wallet.data.data.remote.opensea.NFTAssetResponse
import com.dougie.wallet.data.data.remote.opensea.NFTCollectionInfo
import com.dougie.wallet.data.data.remote.opensea.NFTSingleAssetResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface OpenseaService {
    @GET("assets")
    suspend fun getOwnerAssets(
        @Query("owner") owner: String,
        @Query("asset_contract_address") contractAddress: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("collection") collection: String
    ): NFTAssetResponse

    @GET("collections")
    suspend fun getOwnerCollections(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("asset_owner") owner: String
    ): List<NFTCollectionInfo>

    @GET("asset/{asset_contract_address}/{token_id}")
    suspend fun getSingleAsset(
        @Path("asset_contract_address") contractAddress: String,
        @Path("token_id") tokenId: String,
        @Query("account_address") owner: String
    ): NFTSingleAssetResponse
}
