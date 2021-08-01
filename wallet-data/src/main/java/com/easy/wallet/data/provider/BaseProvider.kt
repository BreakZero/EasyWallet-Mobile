package com.easy.wallet.data.provider

import com.easy.wallet.data.WalletDataSDK
import com.easy.wallet.data.constant.ChainId
import com.easy.wallet.data.network.BlockChairClient
import com.easy.wallet.data.network.BlockChairService
import com.easy.wallet.data.network.testnet.TestNetClient
import com.easy.wallet.data.network.testnet.TestNetService
import wallet.core.jni.HDWallet

abstract class BaseProvider(
    protected val hdWallet: HDWallet
) : IProvider {
    open val currChainId = WalletDataSDK.chainId()
    protected val isMainNet = WalletDataSDK.chainId() == ChainId.MAINNET

    internal var blockChairService = BlockChairClient.client().create(BlockChairService::class.java)
    internal var testNetService = TestNetClient.client().create(TestNetService::class.java)
}
