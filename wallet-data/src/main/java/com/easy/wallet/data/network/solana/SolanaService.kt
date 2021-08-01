package com.easy.wallet.data.network.solana

import com.easy.wallet.data.data.remote.rpc.BaseRPCReq
import com.easy.wallet.data.data.remote.solana.*
import retrofit2.http.Body
import retrofit2.http.POST

internal interface SolanaService {
    @POST(".")
    suspend fun getBalance(
        @Body reqBody: BaseRPCReq<String>
    ): SOLBaseResponse<SOLBalanceResult>

    @POST(".")
    suspend fun broadcastTransaction(
        @Body reqBody: BaseRPCReq<String>
    ): SOLBaseResponse<String>

    @POST(".")
    suspend fun recentBlockHash(
        @Body reqBody: BaseRPCReq<String>
    ): SOLBaseResponse<RecentBlockHashResult>
}
