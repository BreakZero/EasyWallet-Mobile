package com.easy.wallet.data

import android.content.Context
import com.easy.framework.common.BasicStore
import com.easy.wallet.WalletDatabase
import com.easy.wallet.data.constant.CHAINID_STORE_KEY
import com.easy.wallet.data.constant.ChainId
import com.easy.wallet.data.defi.NFTManager
import com.easy.wallet.data.provider.*
import com.easy.wallet.multi.MultiWalletConfig
import com.easy.wallet.multi.model.WalletInfo
import comeasywalletdata.CoinConfig
import wallet.core.jni.HDWallet
import kotlin.properties.Delegates

object WalletDataSDK {
    private var hdWallet: HDWallet? = null
    private var basicStore by Delegates.notNull<BasicStore>()
    private var walletDatabase by Delegates.notNull<WalletDatabase>()

    private var nftManager: NFTManager? = null

    private val provideMap = hashMapOf<String, IProvider>()

    internal fun currWallet(): HDWallet {
        return requireNotNull(hdWallet)
    }

    internal fun injectBasicStore(application: Context) {
        basicStore = BasicStore(application)
    }

    internal fun initDatabase(walletDatabase: WalletDatabase) {
        this.walletDatabase = walletDatabase
    }

    fun chainId(): ChainId {
        return when (basicStore.getString(CHAINID_STORE_KEY)) {
            ChainId.ROPSTEN.name -> ChainId.ROPSTEN
            ChainId.RINKEBY.name -> ChainId.RINKEBY
            ChainId.GÖRLI.name -> ChainId.GÖRLI
            ChainId.KOVAN.name -> ChainId.KOVAN
            else -> ChainId.MAINNET
        }
    }

    fun updateChain(name: String) {
        basicStore.putString(CHAINID_STORE_KEY, name)
    }

    suspend fun injectWallet(passphrase: String = ""): WalletInfo? {
        return MultiWalletConfig.initHDWallet {
            this.hdWallet = HDWallet(it, passphrase)
        }
    }

    suspend fun injectWallet(walletName: String, mnemonic: String?, passphrase: String = ""): Boolean {
        val newWallet = mnemonic?.let {
            HDWallet(it, passphrase)
        } ?: HDWallet(128, passphrase)
        this.hdWallet = newWallet
        return MultiWalletConfig.addWallet(walletName, newWallet.mnemonic())
    }

    fun activeAssets(): List<CoinConfig> {
        return this.walletDatabase.coinConfigQueries.getActives(true).executeAsList()
    }

    fun allAssets(): List<CoinConfig> {
        return this.walletDatabase.coinConfigQueries.getAll().executeAsList()
    }

    fun toggleBySlug(slug: String) {
        this.walletDatabase.coinConfigQueries.toggleBySlug(slug)
    }

    fun injectNFTManager(): NFTManager {
        return nftManager ?: NFTManager(currWallet(), chainId())
    }

    fun injectProvider(
        slug: String,
        symbol: String,
        decimals: Int
    ): IProvider {
        return provideMap[slug] ?: kotlin.run {
            val currProvider = when (slug) {
                "btc-main" -> BitcoinProvider()
                "eth-main" -> EthereumProvider(chainId())
                "atom-main" -> CosmosProvider()
                "dot-main" -> PolkadotProvide()
                "bnb-smart" -> BinanceSmartProvider()
                "bnb-smart-legacy" -> BinanceSmartLegacyProvider()
                "doge-main" -> DogecoinProvider()
                "bitcoin-cash-main" -> BitcoinCashProvider()
                "ada-main" -> CardanoProvider()
                else -> ERC20Provider(
                    symbol = symbol,
                    decimals = decimals,
                    nChainId = chainId()
                )
            }
            provideMap[symbol] = currProvider
            currProvider
        }
    }

    fun notifyChainChanged() {
        nftManager = null
        provideMap.clear()
    }
}