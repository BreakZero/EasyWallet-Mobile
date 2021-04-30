package com.dougie.wallet.data

import android.content.Context
import com.dougie.framework.common.BasicStore
import com.dougie.wallet.WalletDatabase
import com.dougie.wallet.data.constant.CHAINID_STORE_KEY
import com.dougie.wallet.data.constant.ChainId
import com.dougie.wallet.data.defi.NFTManager
import com.dougie.wallet.data.provider.*
import comdougiewalletdata.CoinConfig
import wallet.core.jni.HDWallet
import kotlin.properties.Delegates

object DeFiWalletSDK {
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

    fun initWallet(mnemonic: String, passphrase: String = "") {
        this.hdWallet = HDWallet(mnemonic, passphrase)
    }

    fun activeAssets(): List<CoinConfig> {
        return this.walletDatabase.coinConfigQueries.getActives(true).executeAsList()
    }

    fun allAssets(): List<CoinConfig> {
        return this.walletDatabase.coinConfigQueries.getAll().executeAsList()
    }

    fun toggleBySymbol(symbol: String) {
        this.walletDatabase.coinConfigQueries.toggleBySymbol(symbol)
    }

    fun injectNFTManager(): NFTManager {
        return nftManager ?: NFTManager(currWallet(), chainId())
    }

    fun injectProvider(
        symbol: String,
        decimals: Int
    ): IProvider {
        return provideMap[symbol] ?: kotlin.run {
            val currProvider = when (symbol) {
                "BTC" -> BitcoinProvider()
                "ETH" -> EthereumProvider(chainId())
                "ATOM" -> CosmosProvider()
                "DOT" -> PolkadotProvide()
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
