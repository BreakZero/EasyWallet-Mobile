package com.easy.wallet.data.provider

import com.easy.wallet.data.WalletDataSDK
import com.easy.wallet.data.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import wallet.core.jni.CoinType
import java.math.BigInteger

class CardanoProvider : BaseProvider(WalletDataSDK.currWallet()) {
    override fun getBalance(address: String): Flow<BigInteger> {
        return flow {
            emit(BigInteger.ZERO)
        }.flowOn(Dispatchers.IO)
    }

    override fun getTransactions(
        address: String,
        limit: Int,
        offset: Int
    ): Flow<List<TransactionDataModel>> {
        return flow {
            emit(emptyList<TransactionDataModel>())
        }.flowOn(Dispatchers.IO)
    }

    override fun buildTransactionPlan(sendModel: SendModel): Flow<SendPlanModel> {
        TODO()
    }

    override fun broadcastTransaction(rawData: String): Flow<String> {
        TODO()
    }

    override fun getAddress(isLegacy: Boolean): String {
        return hdWallet.getAddressForCoin(CoinType.CARDANO)
    }

    override fun validateAddress(address: String): Boolean {
        return CoinType.CARDANO.validate(address)
    }
}
