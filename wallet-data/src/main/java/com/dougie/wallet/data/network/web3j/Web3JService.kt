package com.dougie.wallet.data.network.web3j

import com.dougie.wallet.data.constant.APIKey
import com.dougie.wallet.data.constant.ChainId
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
                    else -> "https://ropsten.infura.io/v3/$apiKey"
                },
                httpClient
            )
            Web3j.build(httpService)
        }
    }
}
