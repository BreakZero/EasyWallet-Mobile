package com.easy.wallet.data.network.polkadot

import com.easy.wallet.data.data.remote.dot.*
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

internal interface PolkadotService {
  // DOT
  @POST("https://polkadot.api.subscan.io/api/open/account")
  suspend fun getDOTBalance(
    @Body reqBody: RequestBody
  ): DOTBaseSubscanResponse<DOTBalanceData>

  @POST(".")
  suspend fun getDOTNonce(
    @Body reqBody: PolkadotRPCRequestBody
  ): DOTBaseRPCResponse<Int>

  @POST(".")
  suspend fun getDOTBasicInfo(
    @Body reqBody: PolkadotRPCRequestBody
  ): DOTBaseRPCResponse<BasicInfoResult>

  @POST(".")
  suspend fun broadcastTransaction(
    @Body reqBody: PolkadotRPCRequestBody
  ): DOTBaseRPCResponse<String>

  @POST(".")
  suspend fun getFeeInfo(
    @Body reqBody: PolkadotRPCRequestBody
  ): DOTBaseRPCResponse<DOTFeeResult>

  @POST("https://polkadot.api.subscan.io/api/scan/blocks")
  suspend fun getBlockInfo(
    @Body reqBody: RequestBody
  ): DOTBaseSubscanResponse<Blocks>
}
