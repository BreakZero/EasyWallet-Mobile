package com.easy.wallet.data.network.solana

import com.easy.wallet.data.data.remote.solana.RecentBlockHashResponse
import com.easy.wallet.data.data.remote.solana.SOLBalanceResponse
import com.easy.wallet.data.data.remote.solana.SOLBroadcastResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

internal interface SolanaService {
    @POST(".")
    suspend fun getBalance(
        @Body reqBody: RequestBody
    ): SOLBalanceResponse

    @POST(".")
    suspend fun broadcastTransaction(
        @Body reqBody: RequestBody
    ): SOLBroadcastResponse

    @POST(".")
    suspend fun recentBlockHash(
        @Body reqBody: RequestBody
    ): RecentBlockHashResponse
}
