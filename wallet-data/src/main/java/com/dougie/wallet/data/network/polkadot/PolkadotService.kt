package com.dougie.wallet.data.network.polkadot

import com.dougie.wallet.data.data.remote.dot.*
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

internal interface PolkadotService {
    // DOT
    @POST("https://polkadot.subscan.io/api/open/account")
    suspend fun getDOTBalance(
        @Body reqBody: RequestBody
    ): DOTBalanceResponse

    @POST(".")
    suspend fun getDOTNonce(
        @Body reqBody: PolkadotRPCRequestBody
    ): DOTNonceResponse

    @POST(".")
    suspend fun getDOTBasicInfo(
        @Body reqBody: PolkadotRPCRequestBody
    ): DOTBasicInfoResponse

    @POST(".")
    suspend fun broadcastTransaction(
        @Body reqBody: PolkadotRPCRequestBody
    ): DOTBroadcastResponse

    @POST(".")
    suspend fun getFeeInfo(
        @Body reqBody: PolkadotRPCRequestBody
    ): DOTFeeResponse

    @POST("https://polkadot.subscan.io/api/scan/blocks")
    suspend fun getBlockInfo(
        @Body reqBody: RequestBody
    ): BlockInfoResponse
}
