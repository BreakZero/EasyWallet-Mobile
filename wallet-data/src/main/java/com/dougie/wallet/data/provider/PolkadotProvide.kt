package com.dougie.wallet.data.provider

import com.dougie.wallet.data.DeFiWalletSDK
import com.dougie.wallet.data.data.model.SendModel
import com.dougie.wallet.data.data.model.SendPlanModel
import com.dougie.wallet.data.data.model.TransactionDataModel
import com.dougie.wallet.data.etx.toHexByteArray
import com.dougie.wallet.data.etx.toHexBytesInByteString
import com.dougie.wallet.data.network.polkadot.PolkadotClient
import com.dougie.wallet.data.network.polkadot.PolkadotRPCRequestBody
import com.dougie.wallet.data.network.polkadot.PolkadotService
import com.google.protobuf.ByteString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.web3j.utils.Numeric
import timber.log.Timber
import wallet.core.java.AnySigner
import wallet.core.jni.CoinType
import wallet.core.jni.proto.Polkadot
import java.math.BigInteger

class PolkadotProvide : BaseProvider(DeFiWalletSDK.currWallet()) {

    private val dotClient = PolkadotClient.client().create(PolkadotService::class.java)

    private val genesisHashStr =
        "0x91b171bb158e2d3848fa23a9f1c25182fb8e20313b2c1eb49219da7a70ce90c3".toHexBytesInByteString()

    override fun getBalance(address: String): Flow<BigInteger> {
        return flow {
            val balance = dotClient.getDOTBalance(
                "{\"address\": \"$address\"}".toRequestBody("application/json".toMediaTypeOrNull())
            ).result.balance
            emit(balance.toBigDecimal().movePointRight(10).toBigInteger())
        }
    }

    override fun getTransactions(
        address: String,
        limit: Int,
        offset: Int
    ): Flow<List<TransactionDataModel>> {
        return flow {
            emit(listOf<TransactionDataModel>())
        }
    }

    override fun buildTransactionPlan(sendModel: SendModel): Flow<SendPlanModel> {
        val prvKey = ByteString.copyFrom(hdWallet.getKeyForCoin(CoinType.POLKADOT).data())
        val call = Polkadot.Balance.newBuilder().apply {
            transfer = Polkadot.Balance.Transfer.newBuilder().apply {
                this.value = ByteString.copyFrom(
                    sendModel.amount.toHexByteArray()
                )
                this.toAddress = sendModel.to
            }.build()
        }

        return flow {
            val basicInfo = dotClient.getDOTBasicInfo(
                PolkadotRPCRequestBody(methodName = "state_getRuntimeVersion")
            ).result
            val mNonce = dotClient.getDOTNonce(
                PolkadotRPCRequestBody(
                    methodName = "account_nextIndex",
                    params = listOf(getAddress(false))
                )
            ).result
            val input = Polkadot.SigningInput.newBuilder().apply {
                genesisHash = genesisHashStr
                blockHash = genesisHashStr
                nonce = mNonce.toLong()
                specVersion = basicInfo.specVersion
                network = Polkadot.Network.POLKADOT
                transactionVersion = basicInfo.transactionVersion
                privateKey = prvKey
                balanceCall = call.build()
            }
            val output =
                AnySigner.sign(input.build(), CoinType.POLKADOT, Polkadot.SigningOutput.parser())
            emit(Numeric.toHexString(output.encoded.toByteArray()))
        }.flatMapConcat { rawData ->
            Timber.d("0000000 $rawData")
            flow {
                val fee = dotClient.getFeeInfo(
                    PolkadotRPCRequestBody(
                        methodName = "payment_queryInfo",
                        params = listOf(rawData)
                    )
                ).result.partialFee
                emit(
                    SendPlanModel(
                        amount = sendModel.amount.toBigDecimal().movePointLeft(10),
                        fromAddress = getAddress(false),
                        toAddress = sendModel.to,
                        gasLimit = BigInteger.ONE,
                        gas = fee.toBigDecimal(),
                        feeDecimals = 10,
                        memo = sendModel.memo,
                        rawData = rawData
                    )
                )
            }
        }
    }

    override fun broadcastTransaction(rawData: String): Flow<String> {
        return flow {
            val result = dotClient.broadcastTransaction(
                PolkadotRPCRequestBody(
                    methodName = "author_submitExtrinsic",
                    params = listOf(rawData)
                )
            )
            emit(result.result)
        }
    }

    override fun getAddress(isLegacy: Boolean): String {
        return hdWallet.getAddressForCoin(CoinType.POLKADOT)
    }

    override fun validateAddress(address: String): Boolean {
        return CoinType.POLKADOT.validate(address)
    }
}
