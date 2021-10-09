package com.easy.wallet.data.network.ethereum

import com.easy.wallet.data.data.remote.rpc.BaseRPCReq
import com.easy.wallet.data.data.remote.rpc.BaseRPCResp
import com.easy.wallet.data.data.remote.rpc.FeeHistory
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

internal interface EthRpcCall {
  @POST("{apikey}")
  suspend fun tokenBalanceOf(
    @Path("apikey") apikey: String,
    @Body body: BaseRPCReq<Any>
  ): BaseRPCResp<String>

  @POST("{apikey}")
  suspend fun ethBalance(
    @Path("apikey") apikey: String,
    @Body body: RequestBody
  ): BaseRPCResp<String>

  @POST("{apikey}")
  suspend fun ethFeeHistory(
    @Path("apikey") apikey: String,
    @Body body: BaseRPCReq<Any>
  ): BaseRPCResp<FeeHistory>
}
