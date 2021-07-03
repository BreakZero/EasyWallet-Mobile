package com.easy.wallet.multi

import android.content.Context
import com.easy.wallet.multi.model.WalletInfo
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.AfterVersion
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import kotlin.properties.Delegates

object MultiWalletConfig {
    private var walletDatabase by Delegates.notNull<MultiWalletDatabase>()

    internal fun injectDatabase(application: Context) {
        val supportFactory = SupportFactory(SQLiteDatabase.getBytes("multi".toCharArray()))
        val sqliteDriver = AndroidSqliteDriver(
            schema = MultiWalletDatabase.Schema,
            context = application,
            name = "multi-wallet.db",
            factory = supportFactory,
            callback = AndroidSqliteDriver.Callback(
                schema = MultiWalletDatabase.Schema,
                AfterVersion(1) {
                }
            )
        )
        walletDatabase = MultiWalletDatabase(driver = sqliteDriver)
    }

    suspend fun walletInfo(): WalletInfo? {
        val entity = walletDatabase.walletInfoQueries.getCurrentActive(true).executeAsOneOrNull()
        return entity?.let {
            WalletInfo(walletId = it.wallet_id, walletName = it.wallet_name)
        }
    }

    suspend fun initHDWallet(onMnemonic: (String) -> Unit): WalletInfo? {
        val activeWallet = walletDatabase.walletInfoQueries.getCurrentActive(true).executeAsOneOrNull()
        return activeWallet?.let {
            onMnemonic.invoke(it.wallet_mnemonic)
            WalletInfo(walletId = it.wallet_id, walletName = it.wallet_name)
        }
    }

    suspend fun addWallet(name: String, mnemonic: String): Boolean {
        return kotlin.runCatching {
            walletDatabase.walletInfoQueries.insertWallet(name, mnemonic, true)
        }.isSuccess
    }

    suspend fun removeWallet(name: String): Boolean {
        return kotlin.runCatching {
            walletDatabase.walletInfoQueries.removeWalletByName(name)
        }.isSuccess
    }

    suspend fun walletNames(): List<String> {
        return walletDatabase.walletInfoQueries.allWallets().executeAsList().map {
            it.wallet_name
        }
    }
}