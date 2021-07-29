package com.easy.wallet.data.provider

import com.easy.wallet.data.WalletDataSDK
import com.easy.wallet.data.data.model.SendModel
import com.easy.wallet.data.data.model.SendPlanModel
import com.easy.wallet.data.data.model.TransactionDataModel
import com.google.protobuf.ByteString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import wallet.core.jni.CoinType
import java.math.BigInteger

class NearProvider : BaseProvider(WalletDataSDK.currWallet()) {
    override fun getBalance(address: String): Flow<BigInteger> {
        return flow {
            emit(BigInteger.ZERO)
        }
    }

    override fun getTransactions(
        address: String,
        limit: Int,
        offset: Int
    ): Flow<List<TransactionDataModel>> {
        val prvKey = hdWallet.getKeyForCoin(CoinType.POLKADOT)
        val pubKey = prvKey.publicKeyEd25519.description()
        Timber.d("pubkey: $pubKey")
        return flow { emit(emptyList<TransactionDataModel>()) }
    }

    override fun buildTransactionPlan(sendModel: SendModel): Flow<SendPlanModel> {
        TODO("Not yet implemented")
    }

    override fun broadcastTransaction(rawData: String): Flow<String> {
        return flow {
            emit("")
        }
    }

    override fun getAddress(isLegacy: Boolean): String {
        return hdWallet.getAddressForCoin(CoinType.NEAR)
    }

    override fun validateAddress(address: String): Boolean {
        return CoinType.NEAR.validate(address)
    }
}