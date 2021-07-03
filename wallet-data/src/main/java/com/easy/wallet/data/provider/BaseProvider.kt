package com.easy.wallet.data.provider

import com.dougie.wallet.data.network.BlockChairClient
import com.dougie.wallet.data.network.BlockChairService
import com.dougie.wallet.data.network.testnet.TestNetClient
import com.dougie.wallet.data.network.testnet.TestNetService
import wallet.core.jni.HDWallet

abstract class BaseProvider(
    protected val hdWallet: HDWallet
) : IProvider {
    internal var blockChairService = BlockChairClient.client().create(BlockChairService::class.java)
    internal var testNetService = TestNetClient.client().create(TestNetService::class.java)
}
