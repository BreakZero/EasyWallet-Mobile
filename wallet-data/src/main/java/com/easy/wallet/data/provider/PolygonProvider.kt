package com.easy.wallet.data.provider

import com.easy.wallet.data.WalletDataSDK
import com.easy.wallet.data.data.model.SendModel
import com.easy.wallet.data.data.model.SendPlanModel
import com.easy.wallet.data.data.model.TransactionDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import wallet.core.jni.CoinType
import java.math.BigInteger

class PolygonProvider : BaseProvider(WalletDataSDK.currWallet()) {
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
        return flow {
            emit(emptyList<TransactionDataModel>())
        }
    }

    override fun buildTransactionPlan(sendModel: SendModel): Flow<SendPlanModel> {
        TODO("Not yet implemented")
    }

    override fun broadcastTransaction(rawData: String): Flow<String> {
        TODO("Not yet implemented")
    }

    override fun getAddress(isLegacy: Boolean): String {
        return hdWallet.getAddressForCoin(CoinType.POLYGON)
    }

    override fun validateAddress(address: String): Boolean {
        return CoinType.POLYGON.validate(address)
    }
}
