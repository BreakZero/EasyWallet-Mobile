package com.easy.wallet.data.network.solana

import com.easy.wallet.data.data.remote.solana.*
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

internal interface SolanaService {
    @POST(".")
    suspend fun getBalance(
        @Body reqBody: RequestBody
    ): SOLBaseResponse<SOLBalanceResult>

    @POST(".")
    suspend fun broadcastTransaction(
        @Body reqBody: RequestBody
    ): SOLBaseResponse<String>

    @POST(".")
    suspend fun recentBlockHash(
        @Body reqBody: RequestBody
    ): SOLBaseResponse<RecentBlockHashResult>
}
