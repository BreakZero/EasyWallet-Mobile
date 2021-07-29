package com.easy.wallet.data.network.ethereum

import com.easy.wallet.data.constant.APIKey
import com.easy.wallet.data.constant.ChainId
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

object EthRpcClient {
    private val builder: OkHttpClient.Builder = OkHttpClient.Builder().apply {
        addInterceptor(
            HttpLoggingInterceptor(
                object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
                        Timber.tag("HELLO").d(message)
                    }
                }
            ).apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
    }

    fun client(chainId: ChainId): Retrofit {
        val baseUrl = when (chainId) {
            ChainId.MAINNET -> "https://mainnet.infura.io/v3/"
            ChainId.RINKEBY -> "https://rinkeby.infura.io/v3/"
            ChainId.KOVAN -> "https://kovan.infura.io/v3/"
            ChainId.GÖRLI -> "https://goerli.infura.io/v3/"
            ChainId.ROPSTEN -> "https://ropsten.infura.io/v3/"
            ChainId.BINANCEMAIN -> "https://bsc-mainnet.web3api.com/v1/"
            ChainId.BINANCETEST -> "https://bsc-testnet.web3api.com/v1/"
        }
        return Retrofit.Builder()
            .client(builder.build())
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
}