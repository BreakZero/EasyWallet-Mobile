package com.dougie.wallet.data.initializer

import android.content.Context
import androidx.startup.Initializer
import com.dougie.wallet.WalletDatabase
import com.dougie.wallet.data.BuildConfig
import com.dougie.wallet.data.DeFiWalletSDK
import com.dougie.wallet.data.constant.CurrencyType
import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.AfterVersion
import comdougiewalletdata.CoinConfig
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import timber.log.Timber

class DataModuleInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        System.loadLibrary("TrustWalletCore")

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        DeFiWalletSDK.injectBasicStore(context)
        val supportFactory = SupportFactory(SQLiteDatabase.getBytes("wallet".toCharArray()))
        val sqliteDriver = AndroidSqliteDriver(
            schema = WalletDatabase.Schema,
            context = context,
            name = "wallet.db",
            factory = supportFactory,
            callback = AndroidSqliteDriver.Callback(
                schema = WalletDatabase.Schema,
                AfterVersion(1) {
                }
            )
        )
        val typeAdapter = object : ColumnAdapter<CurrencyType, String> {
            override fun decode(databaseValue: String): CurrencyType =
                CurrencyType.valueOf(databaseValue)

            override fun encode(value: CurrencyType): String = value.name
        }
        val database = WalletDatabase(driver = sqliteDriver, coinConfigAdapter = CoinConfig.Adapter(typeAdapter))
        DeFiWalletSDK.initDatabase(database)
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}
