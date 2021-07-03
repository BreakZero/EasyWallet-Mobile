package com.easy.wallet.multi.initializer

import android.content.Context
import androidx.startup.Initializer
import com.easy.wallet.multi.MultiWalletConfig

class MultiWalletModuleInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        MultiWalletConfig.injectDatabase(context)
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}
