package com.easy.wallet.data.network

import com.easy.wallet.data.constant.APIKey
import com.easy.wallet.data.data.remote.EtherScanResponse
import com.easy.wallet.data.data.remote.blockchair.PushTxResponse
import com.easy.wallet.data.data.remote.blockchair.btc.BitcoinRelatedInfoResponse
import com.easy.wallet.data.data.remote.blockchair.btc.BitcoinTxResponse
import com.easy.wallet.data.data.remote.blockchair.erctoken.TokenRelatedInfoResponse
import retrofit2.http.*

internal interface BlockChairService {
  @GET("{currency_chain}/dashboards/address/{address}")
  suspend fun bitcoinRelatedInfoByAddress(
    @Path("currency_chain") chain: String,
    @Path("address") address: String,
    @Query("limit") limit: Int? = null,
    @Query("offset") offset: Int? = null
  ): BitcoinRelatedInfoResponse

  @GET("{currency_chain}/dashboards/transaction/{txId}")
  suspend fun bitcoinTxByHash(
    @Path("txId") txHash: String
  ): BitcoinTxResponse

  @GET("{currency_chain}/dashboards/transactions/{txHashs}")
  suspend fun bitcoinTxsByHash(
    @Path("currency_chain") chain: String,
    @Path("txHashs") txHashs: String
  ): BitcoinTxResponse

  @FormUrlEncoded
  @POST("{currency_chain}/push/transaction")
  suspend fun pushTransaction(
    @Path("currency_chain") chain: String,
    @Field("data") rawData: String
  ): PushTxResponse

  @GET("https://api.blockchair.com/ethereum/erc-20/{token_address}/dashboards/address/{address}")
  suspend fun tokenRelatedInfoByAddress(
    @Path("token_address") tokenAddress: String,
    @Path("address") address: String,
    @Query("limit") limit: Int? = null,
    @Query("offset") offset: Int? = null
  ): TokenRelatedInfoResponse

  @GET("https://api{chain_name}.etherscan.io/api")
  suspend fun getEtherScanTransactions(
    @Path("chain_name") chainName: String,
    @Query("module") module: String = "account",
    @Query("action") action: String = "txlist",
    @Query("contractaddress") contractaddress: String? = null,
    @Query("address") address: String,
    @Query("sort") sort: String = "desc",
    @Query("page") page: Int = 1,
    @Query("offset") offset: Int = 10,
    @Query("apikey") apikey: String = APIKey.ETHERSCAN_API_KEY
  ): EtherScanResponse

  @GET("https://api{chain_name}.bscscan.com/api")
  suspend fun getBSCScanTransactions(
    @Path("chain_name") chainName: String,
    @Query("module") module: String = "account",
    @Query("action") action: String = "txlist",
    @Query("contractaddress") contractaddress: String? = null,
    @Query("address") address: String,
    @Query("sort") sort: String = "desc",
    @Query("page") page: Int = 1,
    @Query("offset") offset: Int = 10,
    @Query("apikey") apikey: String = APIKey.BSCSCAN_API_KEY
  ): EtherScanResponse
}
