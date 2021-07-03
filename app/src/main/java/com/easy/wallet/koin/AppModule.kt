package com.easy.wallet.koin

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.lifecycle.SavedStateHandle
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.easy.framework.common.BasicStore
import com.easy.wallet.data.CurrencyInfo
import com.easy.wallet.data.WalletDataSDK
import com.easy.wallet.data.provider.IProvider
import com.easy.wallet.feature.MainActivity
import com.easy.wallet.feature.MainViewModel
import com.easy.wallet.feature.coin.CoinListFragment
import com.easy.wallet.feature.coin.CoinListViewModel
import com.easy.wallet.feature.defi.TestDefiFragment
import com.easy.wallet.feature.defi.TestDefiViewModel
import com.easy.wallet.feature.home.HomeFragment
import com.easy.wallet.feature.home.HomeViewModel
import com.easy.wallet.feature.nft.NFTCollectionsFragment
import com.easy.wallet.feature.nft.NFTCollectionsViewModel
import com.easy.wallet.feature.nft.asset.NFTAssetFragment
import com.easy.wallet.feature.nft.asset.NFTAssetViewModel
import com.easy.wallet.feature.nft.assets.NFTAssetsFragment
import com.easy.wallet.feature.nft.assets.NFTAssetsViewModel
import com.easy.wallet.feature.send.SendFragment
import com.easy.wallet.feature.send.SendViewModel
import com.easy.wallet.feature.send.preview.TxPreviewFragment
import com.easy.wallet.feature.send.preview.TxPreviewViewModel
import com.easy.wallet.feature.settings.SettingsFragment
import com.easy.wallet.feature.settings.SettingsViewModel
import com.easy.wallet.feature.settings.chain.SetChainFragment
import com.easy.wallet.feature.settings.chain.SetChainViewModel
import com.easy.wallet.feature.sharing.BrowserFragment
import com.easy.wallet.feature.sharing.QRCodeFragment
import com.easy.wallet.feature.sharing.ScannerFragment
import com.easy.wallet.feature.start.StartActivity
import com.easy.wallet.feature.start.StartViewModel
import com.easy.wallet.feature.start.restore.ImportWalletFragment
import com.easy.wallet.feature.start.restore.ImportWalletViewModel
import com.easy.wallet.feature.start.splash.SplashFragment
import com.easy.wallet.feature.start.splash.SplashViewModel
import com.easy.wallet.feature.transaction.TransactionsFragment
import com.easy.wallet.feature.transaction.TransactionsViewModel
import com.easy.wallet.feature.transaction.detail.TransactionDetailFragment
import com.easy.wallet.feature.transaction.detail.TransactionDetailViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

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
        WalletDataSDK.injectProvider(
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

val scopeModule = module {
    scope<MainActivity> {
        viewModel { MainViewModel() }
    }

    scope<StartActivity> {
        viewModel { StartViewModel() }
    }

    scope<HomeFragment> {
        viewModel { HomeViewModel(get()) }
    }

    scope<TransactionsFragment> {
        viewModel { (asset: CurrencyInfo) ->
            TransactionsViewModel(
                WalletDataSDK.injectProvider(
                    asset.slug,
                    asset.symbol,
                    asset.decimal
                )
            )
        }
    }

    scope<TransactionDetailFragment> {
        viewModel {
            TransactionDetailViewModel()
        }
    }

    scope<CoinListFragment> {
        viewModel {
            CoinListViewModel()
        }
    }

    scope<SendFragment> {
        viewModel { (state: SavedStateHandle, currencyInfo: CurrencyInfo) ->
            SendViewModel(
                state = state,
                currencyInfo = currencyInfo
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
        viewModel { (currencyInfo: CurrencyInfo) ->
            TxPreviewViewModel(
                WalletDataSDK.injectProvider(
                    currencyInfo.slug,
                    currencyInfo.symbol,
                    currencyInfo.decimal
                )
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
