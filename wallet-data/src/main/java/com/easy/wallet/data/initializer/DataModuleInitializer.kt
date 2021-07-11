package com.easy.wallet.data.initializer

import android.content.Context
import androidx.startup.Initializer
import com.easy.wallet.WalletDatabase
import com.easy.wallet.data.BuildConfig
import com.easy.wallet.data.WalletDataSDK
import com.easy.wallet.data.constant.CurrencyType
import com.easy.wallet.multi.initializer.MultiWalletModuleInitializer
import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.android.AndroidSqliteDriver
import comeasywalletdata.CoinConfig
import timber.log.Timber

class DataModuleInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        System.loadLibrary("TrustWalletCore")

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        WalletDataSDK.injectBasicStore(context)
        val sqliteDriver = AndroidSqliteDriver(
            schema = WalletDatabase.Schema,
            context = context,
            name = "wallet.db"
        )
        val typeAdapter = object : ColumnAdapter<CurrencyType, String> {
            override fun decode(databaseValue: String): CurrencyType =
                CurrencyType.valueOf(databaseValue)

            override fun encode(value: CurrencyType): String = value.name
        }
        val database = WalletDatabase(
            driver = sqliteDriver,
            coinConfigAdapter = CoinConfig.Adapter(typeAdapter)
        )
        WalletDataSDK.initDatabase(database)
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(MultiWalletModuleInitializer::class.java)
    }
}
