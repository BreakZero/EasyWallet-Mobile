package com.easy.wallet.data.network.cosmos

import com.easy.wallet.data.constant.APIKey
import com.easy.wallet.data.data.remote.cosmos.*
import com.easy.wallet.data.data.remote.cosmos.AccountInfo
import com.easy.wallet.data.data.remote.cosmos.BalanceResponse
import com.easy.wallet.data.data.remote.cosmos.BroadcastResponse
import com.easy.wallet.data.data.remote.cosmos.ValidatorResponse
import com.easy.wallet.data.data.remote.cosmos.request.CosmosTxRequest
import okhttp3.RequestBody
import retrofit2.http.*

internal interface FigmentService {
    @GET("auth/accounts/{address}")
    suspend fun atomAccount(
        @Path("address") address: String
    ): AccountInfo

    @GET("bank/balances/{address}")
    suspend fun atomBalance(
        @Path("address") address: String
    ): BalanceResponse

    @GET("staking/delegators/{delegatorAddr}/validators")
    suspend fun delegations(
        @Path("delegatorAddr") delegatorAddr: String
    ): ValidatorResponse

    @POST("txs")
    suspend fun broadcastTransaction(
        @Body jsonSerial: RequestBody
    ): BroadcastResponse

    @POST("")
    suspend fun getTransactions(
        @Url url: String = "https://cosmos--search.datahub.figment.io/transactions_search/apikey/${APIKey.FIGMENT_API_KEY}/",
        @Body requestBody: CosmosTxRequest
    ): List<TransactionResponse>
}
