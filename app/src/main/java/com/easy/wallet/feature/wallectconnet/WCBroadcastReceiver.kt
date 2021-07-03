package com.easy.wallet.feature.wallectconnet

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dougie.wallet.feature.wallectconnet.data.WCDataWrap

class WCBroadcastReceiver(
    private val callback: (WCDataWrap) -> Unit
) : BroadcastReceiver() {

    companion object {
        const val WC_ACTION_FILTER_ACTION = "wallet.connect.action"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val data = intent.getParcelableExtra<WCDataWrap>(WalletConnectService.KEY_WC_DATA)
        data?.also {
            callback.invoke(data)
        }
    }
}
