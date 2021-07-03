package com.easy.wallet.data.provider

import com.easy.wallet.data.DeFiWalletSDK
import com.easy.wallet.data.constant.ChainId
import com.easy.wallet.data.data.model.*
import com.easy.wallet.data.error.InsufficientBalanceException
import com.easy.wallet.data.etx.toHexByteArray
import com.easy.wallet.data.network.web3j.Web3JService
import com.google.protobuf.ByteString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.utils.Numeric
import timber.log.Timber
import wallet.core.java.AnySigner
import wallet.core.jni.CoinType
import wallet.core.jni.proto.Ethereum
import java.math.BigDecimal
import java.math.BigInteger

class CardanoProvider : BaseProvider(DeFiWalletSDK.currWallet()) {
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
