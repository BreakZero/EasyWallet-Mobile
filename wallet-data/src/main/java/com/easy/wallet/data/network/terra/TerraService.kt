package com.easy.wallet.data.network.terra

import com.easy.wallet.data.data.remote.terra.AccountResponse
import com.easy.wallet.data.data.remote.terra.BaseTxModel
import com.easy.wallet.data.data.remote.terra.TerraBalance
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

internal interface TerraService {
  @GET("cosmos/bank/v1beta1/balances/{address}")
  suspend fun getBalance(
    @Path("address") address: String
  ): TerraBalance

  @GET("cosmos/auth/v1beta1/accounts/{address}")
  suspend fun getAccountInfo(
    @Path("address") address: String
  ): AccountResponse

  @POST("cosmos/tx/v1beta1/txs")
  suspend fun broadcastTransaction(
    @Body reqBody: BaseTxModel
  ): ResponseBody
}
