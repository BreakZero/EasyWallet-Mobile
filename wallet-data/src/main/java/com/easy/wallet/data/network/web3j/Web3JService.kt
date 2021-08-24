package com.easy.wallet.data.network.web3j

import com.easy.wallet.data.constant.APIKey
import com.easy.wallet.data.constant.ChainId
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import timber.log.Timber

internal object Web3JService {
  private const val apiKey = APIKey.INFURA_API_KEY
  private val httpClient = OkHttpClient.Builder().addInterceptor(
    HttpLoggingInterceptor(
      object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
          Timber.d(message)
        }
      }
    ).apply {
      level = HttpLoggingInterceptor.Level.BODY
    }
  ).build()
  private var web3j: Web3j? = null

  fun web3jClient(chainId: ChainId): Web3j {
    return web3j ?: kotlin.run {
      val httpService = HttpService(
        when (chainId) {
          ChainId.MAINNET -> "https://mainnet.infura.io/v3/$apiKey"
          ChainId.RINKEBY -> "https://rinkeby.infura.io/v3/$apiKey"
          ChainId.KOVAN -> "https://kovan.infura.io/v3/$apiKey"
          ChainId.GÖRLI -> "https://goerli.infura.io/v3/$apiKey"
          ChainId.ROPSTEN -> "https://ropsten.infura.io/v3/$apiKey"
          ChainId.BINANCEMAIN -> "https://bsc-mainnet.web3api.com/v1/${APIKey.BSCRPC_APIKEY}"
          ChainId.BINANCETEST -> "https://bsc-testnet.web3api.com/v1/${APIKey.BSCRPC_APIKEY}"
        },
        httpClient
      )
      Web3j.build(httpService)
    }
  }
}
