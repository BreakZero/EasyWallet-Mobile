package com.easy.wallet.data.provider

import com.easy.wallet.data.network.BlockChairClient
import com.easy.wallet.data.network.BlockChairService
import com.easy.wallet.data.network.testnet.TestNetClient
import com.easy.wallet.data.network.testnet.TestNetService
import wallet.core.jni.HDWallet

abstract class BaseProvider(
    protected val hdWallet: HDWallet
) : IProvider {
    internal var blockChairService = BlockChairClient.client().create(BlockChairService::class.java)
    internal var testNetService = TestNetClient.client().create(TestNetService::class.java)
}
