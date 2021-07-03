package com.easy.wallet.data.provider

import com.easy.wallet.data.DeFiWalletSDK
import com.easy.wallet.data.data.model.*
import com.easy.wallet.data.error.InsufficientBalanceException
import com.google.protobuf.ByteString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import org.web3j.utils.Numeric
import wallet.core.java.AnySigner
import wallet.core.jni.AnyAddress
import wallet.core.jni.BitcoinAddress
import wallet.core.jni.BitcoinSigHashType
import wallet.core.jni.CoinType
import wallet.core.jni.proto.Bitcoin
import wallet.core.jni.proto.Common
import java.math.BigDecimal
import java.math.BigInteger

class BitcoinProvider : BaseProvider(DeFiWalletSDK.currWallet()) {
    companion object {
        private const val DECIMALS = 8
    }
    @ExperimentalUnsignedTypes
    override fun getBalance(address: String): Flow<BigInteger> {
        return flow {
            val balance = blockChairService.bitcoinRelatedInfoByAddress(
                "bitcoin",
                address,
                limit = 0,
                offset = 0
            ).data.values.first().addressInfo.balance.toBigInteger()
            emit(balance)
        }.flowOn(Dispatchers.IO)
    }

    override fun getTransactions(
        address: String,
        limit: Int,
        offset: Int
    ): Flow<List<TransactionDataModel>> {
        return flow {
            val result =
                blockChairService.bitcoinRelatedInfoByAddress("bitcoin", address, limit, offset)
                    .data.values.first().transactionsHash
            emit(result.joinToString(separator = ","))
        }.flatMapConcat { it ->
            flow {
                val result = blockChairService.bitcoinTxsByHash("bitcoin", it)
                    .data.values.map { it ->
                        val isSend = address.equals(it.inputs.first().recipient, true).not()
                        TransactionDataModel(
                            txHash = it.transaction.hash,
                            time = it.transaction.time,
                            recipient = if (isSend) {
                                it.outputs.find {
                                    address.equals(it.recipient, true).not()
                                }?.recipient?.ifBlank { "" }.orEmpty()
                            } else address,
                            sender = if (isSend) {
                                address
                            } else {
                                it.outputs.find {
                                    address.equals(it.recipient, true).not()
                                }?.recipient?.ifBlank { "" }.orEmpty()
                            },
                            amount = it.transaction.outputTotal.toBigDecimalOrNull()
                                ?: BigDecimal.ZERO,
                            direction = if (isSend) TxDirection.SEND else TxDirection.RECEIVE,
                            status = when {
                                (it.transaction.blockId == -1L) and it.transaction.hash.isNotBlank() -> TxStatus.PENDING
                                (it.transaction.blockId > 0L) and it.transaction.hash.isNotBlank() -> TxStatus.CONFIRM
                                else -> TxStatus.FAILURE
                            },
                            decimal = DECIMALS,
                            symbol = "BTC"
                        )
                    }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun buildTransactionPlan(sendModel: SendModel): Flow<SendPlanModel> {
        val from = getAddress(sendModel.to.startsWith("1"))
        val sendAmount = sendModel.amount

        return flow {
            val info =
                blockChairService.bitcoinRelatedInfoByAddress("bitcoin", from).data.values.first()
            emit(info)
        }.map {
            if ((it.addressInfo.balance.toBigInteger() < sendAmount)
                and !sendModel.useMax
            ) throw InsufficientBalanceException()

            val hexScript = it.addressInfo.scriptHex
            val utxos = it.utxos
            val input = Bitcoin.SigningInput.newBuilder().apply {
                amount = sendAmount.toLong()
                hashType = BitcoinSigHashType.ALL.value().or(BitcoinSigHashType.FORK.value())
                toAddress = sendModel.to
                changeAddress = from
                byteFee = sendModel.feeByte.toLong()
                useMaxAmount = sendModel.useMax
                coinType = CoinType.BITCOIN.value()
            }
            val prvData = hdWallet.getKeyForCoin(CoinType.BITCOIN)
            utxos.forEach {
                input.addPrivateKey(ByteString.copyFrom(prvData.data()))
                val outPoint = Bitcoin.OutPoint.newBuilder()
                    .setHash(
                        ByteString.copyFrom(
                            Numeric.hexStringToByteArray(
                                it.transactionHash
                            ).reversed().toByteArray()
                        )
                    ).setIndex(it.index)
                    .setSequence(Long.MAX_VALUE.toInt())
                    .build()
                val script = ByteString.copyFrom(
                    Numeric.hexStringToByteArray(hexScript)
                )

                val unspentTransaction =
                    Bitcoin.UnspentTransaction
                        .newBuilder()
                        .setOutPoint(outPoint)
                        .setAmount(it.value.toLong())
                        .setScript(script)
                        .build()

                input.addUtxo(unspentTransaction)
            }
            val plan = AnySigner.plan(
                input.build(),
                CoinType.BITCOIN,
                Bitcoin.TransactionPlan.parser()
            )
            input.plan = plan
            val output = AnySigner.sign(
                input.build(),
                CoinType.BITCOIN,
                Bitcoin.SigningOutput.parser()
            )
            val rawData = Numeric.toHexString(output.encoded.toByteArray())
            if (output.error == Common.SigningError.OK) {
                SendPlanModel(
                    amount = plan.amount.toBigDecimal().movePointLeft(DECIMALS),
                    toAddress = sendModel.to,
                    fromAddress = from,
                    gas = plan.fee.toBigDecimal(),
                    rawData = rawData,
                    memo = sendModel.memo,
                    feeDecimals = DECIMALS,
                )
            } else {
                SendPlanModel(
                    gas = sendModel.feeByte.toBigDecimal(),
                    feeDecimals = DECIMALS,
                )
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun broadcastTransaction(rawData: String): Flow<String> {
        return flow {
            val result = blockChairService.pushTransaction("bitcoin", rawData).data.txHash
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    override fun getAddress(isLegacy: Boolean): String {
        return if (!isLegacy) hdWallet.getAddressForCoin(CoinType.BITCOIN)
        else hdWallet.getKey(CoinType.BITCOIN, "m/44'/0'/0'/0/0").getPublicKeySecp256k1(true)
            .let { publicKey ->
                BitcoinAddress(publicKey, 0.toByte()).description()
            }
    }

    override fun validateAddress(address: String): Boolean {
        return AnyAddress.isValid(address, CoinType.BITCOIN)
    }
}
