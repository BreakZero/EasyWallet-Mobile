package com.easy.wallet.data.provider

import com.easy.wallet.data.DeFiWalletSDK
import com.easy.wallet.data.data.model.*
import com.easy.wallet.data.data.remote.cosmos.request.CosmosTxRequest
import com.easy.wallet.data.error.InsufficientBalanceException
import com.easy.wallet.data.network.cosmos.FigmentClient
import com.easy.wallet.data.network.cosmos.FigmentService
import com.google.protobuf.ByteString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import wallet.core.java.AnySigner
import wallet.core.jni.AnyAddress
import wallet.core.jni.CoinType
import wallet.core.jni.proto.Cosmos
import java.math.BigDecimal
import java.math.BigInteger

class CosmosProvider : BaseProvider(DeFiWalletSDK.currWallet()) {
    companion object {
        private const val DECIMALS = 6
    }

    private val figmentClient = FigmentClient.client().create(FigmentService::class.java)

    override fun getBalance(address: String): Flow<BigInteger> {
        return flow {
            val balance = figmentClient.atomBalance(
                address
            ).result.first().amount.toBigInteger()
            emit(balance)
        }.flowOn(Dispatchers.IO)
    }

    override fun getTransactions(
        address: String,
        limit: Int,
        offset: Int
    ): Flow<List<TransactionDataModel>> {
        return flow {
            val result = figmentClient.getTransactions(
                requestBody = CosmosTxRequest(
                    network = "cosmos",
                    limit = limit,
                    chainIds = listOf("cosmoshub-3"),
                    account = listOf(getAddress(false))
                )
            ).map {
                TransactionDataModel(
                    txHash = it.hash,
                    recipient = it.blockHash,
                    amount = BigDecimal.ZERO,
                    time = it.time,
                    direction = TxDirection.SEND,
                    status = TxStatus.PENDING,
                    decimal = DECIMALS,
                    symbol = "ATOM",
                    sender = getAddress(false)
                )
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    override fun buildTransactionPlan(sendModel: SendModel): Flow<SendPlanModel> {
        return flow {
            val result = figmentClient.atomBalance(getAddress(false))
            emit(result.result.first())
        }.flatMapConcat {
            if (it.amount.toBigInteger() < sendModel.amount) throw InsufficientBalanceException()
            flow {
                val atomAccount = figmentClient.atomAccount(getAddress(false))
                emit(atomAccount.result.accountValue)
            }
        }.map {
            val prvKey = ByteString.copyFrom(hdWallet.getKeyForCoin(CoinType.COSMOS).data())

            val txAmount = Cosmos.Amount.newBuilder().apply {
                amount = sendModel.amount.toLong()
                denom = "uatom"
            }.build()

//            val sendCoinsMsg = Cosmos.Message.Send.newBuilder().apply {
//                fromAddress = getAddress(false)
//                toAddress = sendModel.to
//                addAllAmounts(listOf(txAmount))
//            }.build()
            val redelegateMsg = Cosmos.Message.BeginRedelegate.newBuilder().apply {
                validatorSrcAddress = "cosmosvaloper1hjct6q7npsspsg3dgvzk3sdf89spmlpfdn6m9d"
                validatorDstAddress = sendModel.to
                delegatorAddress = getAddress(false)
                amount = txAmount
            }.build()

            val unbondingMsg = Cosmos.Message.Undelegate.newBuilder().apply {
                validatorAddress = sendModel.to
                delegatorAddress = getAddress(false)
                amount = txAmount
            }.build()

            val message = Cosmos.Message.newBuilder().apply {
                restakeMessage = redelegateMsg
            }.build()
            val unMsg = Cosmos.Message.newBuilder().apply {
                unstakeMessage = unbondingMsg
            }.build()

            val feeAmount = Cosmos.Amount.newBuilder().apply {
                amount = 0
                denom = "uatom"
            }.build()

            val cosmosFee = Cosmos.Fee.newBuilder().apply {
                gas = 500000
                addAmounts(feeAmount)
            }.build()

            val signingInput = Cosmos.SigningInput.newBuilder().apply {
                accountNumber = it.accountNumber.toLong()
                chainId = "cosmoshub-3"
                memo = sendModel.memo
                sequence = it.sequence.toLong()
                mergeFee(cosmosFee)
                privateKey = prvKey
                mode = Cosmos.BroadcastMode.ASYNC
                addAllMessages(listOf(message, unMsg))
            }.build()

            val output =
                AnySigner.sign(signingInput, CoinType.COSMOS, Cosmos.SigningOutput.parser())
            SendPlanModel(
                amount = sendModel.amount.toBigDecimal().movePointLeft(DECIMALS),
                fromAddress = getAddress(false),
                toAddress = sendModel.to,
                gas = 1000.toBigDecimal(),
                memo = sendModel.memo,
                feeDecimals = DECIMALS,
                rawData = output.json
            )
        }
    }

    override fun broadcastTransaction(rawData: String): Flow<String> {
        return flow {
            val body = rawData.toRequestBody("application/json".toMediaTypeOrNull())
            emit(figmentClient.broadcastTransaction(body))
        }.map {
            it.hash
        }
    }

    override fun getAddress(isLegacy: Boolean): String {
        return hdWallet.getAddressForCoin(CoinType.COSMOS)
    }

    override fun validateAddress(address: String): Boolean {
        return AnyAddress.isValid(address, CoinType.COSMOS)
    }
}
