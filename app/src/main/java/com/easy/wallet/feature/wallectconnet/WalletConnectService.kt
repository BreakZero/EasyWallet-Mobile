package com.easy.wallet.feature.wallectconnet

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.easy.wallet.data.constant.ChainId
import com.easy.wallet.data.provider.EthereumProvider
import com.easy.wallet.feature.wallectconnet.data.WCActionType
import com.easy.wallet.feature.wallectconnet.data.WCDataWrap
import com.trustwallet.walletconnect.WCClient
import com.trustwallet.walletconnect.models.WCPeerMeta
import com.trustwallet.walletconnect.models.session.WCSession
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class WalletConnectService : Service() {
    companion object {
        private val ethereumProvider = EthereumProvider()
        private var wcClient by Delegates.notNull<WCClient>()
        const val EXTRA_KEY_WC_URI = "wc-uri-key"
        const val KEY_WC_DATA = "key-wc-data"

        fun approve(id: Long) {
            if (id == 0L) {
                wcClient.approveSession(
                    listOf(ethereumProvider.getAddress(false)),
                    ChainId.MAINNET.id
                )
            } else {
                wcClient.approveRequest(id, null)
            }
        }

        fun reject(id: Long) {
            if (id == 0L) wcClient.rejectSession()
            else wcClient.rejectRequest(id)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        val client = OkHttpClient.Builder().pingInterval(2500, TimeUnit.MILLISECONDS).build()
        wcClient = WCClient(httpClient = client)
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.getStringExtra(EXTRA_KEY_WC_URI)?.also {
            WCSession.from(it)?.also { session ->
                wcClient.connect(
                    session,
                    WCPeerMeta(
                        name = "",
                        url = "url"
                    )
                )
            }
            initWCClientCallback()
        }
        return START_STICKY
    }

    private fun initWCClientCallback() {
        wcClient.onSessionRequest = { _, peerData ->
            sendActionMessage(
                WCDataWrap(
                    wcActionType = WCActionType.CONNECT,
                    orgIcon = peerData.icons.firstOrNull(),
                    orgName = peerData.name,
                    orgUrl = peerData.url,
                    methodId = 0L
                )
            )
        }
        wcClient.onEthSign = { id, data ->
            sendActionMessage(
                WCDataWrap(
                    wcActionType = WCActionType.ETH_SIGN,
                    orgIcon = wcClient.peerMeta?.icons?.firstOrNull(),
                    orgName = wcClient.peerMeta?.name,
                    orgUrl = wcClient.peerMeta?.url,
                    methodId = id,
                    resultData = data.data
                )
            )
        }
        wcClient.onEthSendTransaction = { id, data ->
            sendActionMessage(
                WCDataWrap(
                    wcActionType = WCActionType.ETH_SEND,
                    orgIcon = wcClient.peerMeta?.icons?.firstOrNull(),
                    orgName = wcClient.peerMeta?.name,
                    orgUrl = wcClient.peerMeta?.url,
                    methodId = id,
                    resultData = data.data,
                    toAddress = data.to,
                    gasLimit = data.gasLimit,
                    ethValue = data.value
                )
            )
        }
        wcClient.onEthSignTransaction = { id, data ->
            sendActionMessage(
                WCDataWrap(
                    wcActionType = WCActionType.ETH_SEND,
                    orgIcon = wcClient.peerMeta?.icons?.firstOrNull(),
                    orgName = wcClient.peerMeta?.name,
                    orgUrl = wcClient.peerMeta?.url.orEmpty(),
                    methodId = id,
                    resultData = data.data,
                    toAddress = data.to,
                    gasLimit = data.gasLimit,
                    ethValue = data.value
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        wcClient.disconnect()
        wcClient.killSession()
    }

    private fun sendActionMessage(wrapData: WCDataWrap) {
        Intent().also { intent ->
            intent.action = WCBroadcastReceiver.WC_ACTION_FILTER_ACTION
            intent.putExtra(KEY_WC_DATA, wrapData)
            sendBroadcast(intent)
        }
    }
}
