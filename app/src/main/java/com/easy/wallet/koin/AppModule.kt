package com.easy.wallet.koin

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.dougie.framework.common.BasicStore
import com.dougie.wallet.data.CurrencyInfo
import com.dougie.wallet.data.DeFiWalletSDK
import com.dougie.wallet.data.provider.*
import com.dougie.wallet.feature.MainActivity
import com.dougie.wallet.feature.MainViewModel
import com.dougie.wallet.feature.coin.CoinListFragment
import com.dougie.wallet.feature.coin.CoinListViewModel
import com.dougie.wallet.feature.defi.TestDefiFragment
import com.dougie.wallet.feature.defi.TestDefiViewModel
import com.dougie.wallet.feature.home.HomeFragment
import com.dougie.wallet.feature.home.HomeViewModel
import com.dougie.wallet.feature.nft.NFTCollectionsFragment
import com.dougie.wallet.feature.nft.NFTCollectionsViewModel
import com.dougie.wallet.feature.nft.asset.NFTAssetFragment
import com.dougie.wallet.feature.nft.asset.NFTAssetViewModel
import com.dougie.wallet.feature.nft.assets.NFTAssetsFragment
import com.dougie.wallet.feature.nft.assets.NFTAssetsViewModel
import com.dougie.wallet.feature.send.SendFragment
import com.dougie.wallet.feature.send.SendViewModel
import com.dougie.wallet.feature.send.preview.TxPreviewFragment
import com.dougie.wallet.feature.send.preview.TxPreviewViewModel
import com.dougie.wallet.feature.settings.SettingsFragment
import com.dougie.wallet.feature.settings.SettingsViewModel
import com.dougie.wallet.feature.settings.chain.SetChainFragment
import com.dougie.wallet.feature.settings.chain.SetChainViewModel
import com.dougie.wallet.feature.sharing.BrowserFragment
import com.dougie.wallet.feature.sharing.QRCodeFragment
import com.dougie.wallet.feature.sharing.ScannerFragment
import com.dougie.wallet.feature.start.StartActivity
import com.dougie.wallet.feature.start.StartViewModel
import com.dougie.wallet.feature.start.restore.ImportWalletFragment
import com.dougie.wallet.feature.start.restore.ImportWalletViewModel
import com.dougie.wallet.feature.start.splash.SplashFragment
import com.dougie.wallet.feature.start.splash.SplashViewModel
import com.dougie.wallet.feature.transaction.TransactionFragment
import com.dougie.wallet.feature.transaction.TransactionViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent

val appModule = module {

    single {
        BasicStore(androidApplication())
    }
    single {
        val builder = KeyGenParameterSpec.Builder(
            MasterKey.DEFAULT_MASTER_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE)

        val masterKeyAlias = MasterKey.Builder(androidContext())
            .setKeyGenParameterSpec(builder.build())
            .build()

        EncryptedSharedPreferences.create(
            androidContext(),
            "wallet_crypto_file",
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    factory { (currency: CurrencyInfo) ->
        DeFiWalletSDK.injectProvider(
            currency.slug,
            currency.symbol,
            currency.decimal
        )
    } bind IProvider::class
}

internal object ScopeConst {
    const val FLOW_SESSION_NAME = "flowSession"
    const val FLOW_SESSION_ID = "sendFlowSession"
}

@OptIn(KoinApiExtension::class)
val scopeModule = module {
    scope<MainActivity> {
        viewModel { MainViewModel() }
    }

    scope(named(ScopeConst.FLOW_SESSION_NAME)) {
        scoped { (currency: CurrencyInfo) ->
            DeFiWalletSDK.injectProvider(
                currency.slug,
                currency.symbol,
                currency.decimal
            )
        } bind IProvider::class
    }

    scope<StartActivity> {
        viewModel { StartViewModel() }
    }

    scope<HomeFragment> {
        viewModel { HomeViewModel(get()) }
    }

    scope<TransactionFragment> {
        viewModel { (coinProvider: IProvider) ->
            TransactionViewModel(coinProvider)
        }
    }

    scope<CoinListFragment> {
        viewModel {
            CoinListViewModel()
        }
    }

    scope<SendFragment> {
        viewModel { (currencyInfo: CurrencyInfo) ->
            SendViewModel(
                currencyInfo = currencyInfo,
                state = get(),
                coinProvider = KoinJavaComponent.getKoin().getScope(ScopeConst.FLOW_SESSION_ID)
                    .get()
            )
        }
    }

    scope<ImportWalletFragment> {
        viewModel { ImportWalletViewModel() }
    }

    scope<SettingsFragment> {
        viewModel { SettingsViewModel() }
    }
    scope<SetChainFragment> {
        viewModel { SetChainViewModel() }
    }

    scope<ScannerFragment> {
    }

    scope<QRCodeFragment> {
    }

    scope<TestDefiFragment> {
        viewModel {
            TestDefiViewModel()
        }
    }
    scope<BrowserFragment> {
    }
    scope<SplashFragment> {
        viewModel { SplashViewModel() }
    }
    scope<TxPreviewFragment> {
        viewModel {
            TxPreviewViewModel(
                coinProvider = KoinJavaComponent.getKoin().getScope(ScopeConst.FLOW_SESSION_ID)
                    .get()
            )
        }
    }

    scope<NFTCollectionsFragment> {
        viewModel {
            NFTCollectionsViewModel()
        }
    }
    scope<NFTAssetsFragment> {
        viewModel {
            NFTAssetsViewModel()
        }
    }
    scope<NFTAssetFragment> {
        viewModel {
            NFTAssetViewModel()
        }
    }
}
