package com.easy.wallet.data.network.testnet

import com.easy.wallet.data.data.remote.testnet.StateResponse
import com.easy.wallet.data.data.remote.testnet.TransactionResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TestNetService {
  @GET("eth/testnet/v1/blockchain/address/state/{address}")
  suspend fun ethBalance(
    @Path("address") address: String
  ): StateResponse

  @GET("eth/testnet/v1/blockchain/address/transactions/{address}")
  suspend fun ethTransactions(
    @Path("address") address: String,
    @Query("limit") limit: Int = 20,
    @Query("page") page: Int
  ): TransactionResponse
}
