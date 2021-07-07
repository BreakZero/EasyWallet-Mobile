package com.easy.wallet.data.provider

import com.easy.wallet.data.WalletDataSDK
import com.easy.wallet.data.constant.ChainId
import com.easy.wallet.data.data.model.*
import com.easy.wallet.data.error.InsufficientBalanceException
import com.easy.wallet.data.etx.toHexByteArray
import com.easy.wallet.data.network.web3j.Web3JService
import com.google.protobuf.ByteString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.utils.Numeric
import timber.log.Timber
import wallet.core.java.AnySigner
import wallet.core.jni.AnyAddress
import wallet.core.jni.CoinType
import wallet.core.jni.proto.Ethereum
import java.math.BigInteger

class EthereumProvider(
    private val nChainId: ChainId = ChainId.MAINNET
) : BaseProvider(WalletDataSDK.currWallet()) {
    private val web3JService: Web3j = Web3JService.web3jClient(nChainId)

    companion object {
        private const val GAS_LIMIT = 21000
    }

    override fun getBalance(address: String): Flow<BigInteger> {
        return flow {
            val result = web3JService.ethGetBalance(address, DefaultBlockParameterName.LATEST).sendAsync().get()
            emit(result.balance)
        }.flowOn(Dispatchers.IO)
    }

    override fun getTransactions(
        address: String,
        limit: Int,
        offset: Int
    ): Flow<List<TransactionDataModel>> {
        val page = offset.div(limit).plus(1)
        return flow {
            val response = blockChairService.getEtherScanTransactions(
                chainName = if (nChainId == ChainId.MAINNET) "" else "-${nChainId.name.toLowerCase()}",
                address = address,
                page = page,
                offset = offset
            )
            val result = response.result.map {
                val isSend = it.from.equals(getAddress(false), true)
                TransactionDataModel.ofEthereumType(it, "ETH", 18, isSend)
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun getRawData(dAppSendModel: DAppSendModel): Flow<String> {
        return flow {
            val nonce = web3JService.ethGetTransactionCount(
                getAddress(false),
                DefaultBlockParameterName.LATEST
            ).sendAsync().get().transactionCount
            emit(nonce ?: throw IllegalStateException("get nonce error"))
        }.map {
            val prvKey = ByteString.copyFrom(hdWallet.getKeyForCoin(CoinType.ETHEREUM).data())
            val signingInput = Ethereum.SigningInput.newBuilder().apply {
                privateKey = prvKey
                toAddress = dAppSendModel.to
                chainId = ByteString.copyFrom("01".toBigInteger().toByteArray())
                nonce = ByteString.copyFrom((it).toHexByteArray())
                gasPrice = ByteString.copyFrom(
                    Numeric.cleanHexPrefix(dAppSendModel.gasPrice).toBigInteger(16).toByteArray()
                )
                gasLimit = ByteString.copyFrom(
                    Numeric.cleanHexPrefix(dAppSendModel.gasLimit).toBigInteger(16).toByteArray()
                )
                    transaction = Ethereum.Transaction.newBuilder().apply {
                        erc20Transfer = Ethereum.Transaction.ERC20Transfer.newBuilder().apply {
                            to = dAppSendModel.to
                            amount = ByteString.copyFrom(
                                Numeric.cleanHexPrefix(dAppSendModel.value).toBigInteger(16).toByteArray()
                            )
                        }.build()
                    }.build()
            }
            val encoded = AnySigner.encode(signingInput.build(), CoinType.ETHEREUM)
            val rawData = Numeric.toHexString(encoded)
            rawData
        }
    }

    override fun buildTransactionPlan(sendModel: SendModel): Flow<SendPlanModel> {
        return flow {
            val balance = web3JService
                .ethGetBalance(getAddress(false), DefaultBlockParameterName.LATEST)
                .sendAsync().get().balance
            val nonce = web3JService.ethGetTransactionCount(
                getAddress(false),
                DefaultBlockParameterName.LATEST
            ).sendAsync().get().transactionCount
            emit(Pair(balance, nonce))
        }.map {
            if (it.first < sendModel.amount) throw InsufficientBalanceException()
            val prvKey = ByteString.copyFrom(hdWallet.getKeyForCoin(CoinType.ETHEREUM).data())
            val txBuild = Ethereum.Transaction.newBuilder().apply {
                transfer = Ethereum.Transaction.Transfer.newBuilder().apply {
                    amount = ByteString.copyFrom(
                        sendModel.amount.toHexByteArray()
                    )
                }.build()
            }
            val signingInput = Ethereum.SigningInput.newBuilder().apply {
                privateKey = prvKey
                toAddress = sendModel.to
                chainId = ByteString.copyFrom(nChainId.id.toBigInteger().toByteArray())
                nonce = ByteString.copyFrom(it.second.toHexByteArray())
                gasPrice = ByteString.copyFrom(
                    sendModel.feeByte.toBigDecimal().movePointRight(9).toBigInteger()
                        .toHexByteArray()
                )
                gasLimit = ByteString.copyFrom(GAS_LIMIT.toHexByteArray())
                transaction = txBuild.build()
            }

            val encoded = AnySigner.encode(signingInput.build(), CoinType.ETHEREUM)
            val rawData = Numeric.toHexString(encoded)
            Timber.d(String(AnySigner.decode(rawData.toHexByteArray(), CoinType.ETHEREUM)))
            SendPlanModel(
                amount = sendModel.amount.toBigDecimal().movePointLeft(18),
                fromAddress = getAddress(false),
                toAddress = sendModel.to,
                gasLimit = GAS_LIMIT.toBigInteger(),
                gas = sendModel.feeByte.toBigDecimal(),
                feeDecimals = 9,
                memo = sendModel.memo,
                rawData = rawData
            )
        }
    }

    override fun broadcastTransaction(rawData: String): Flow<String> {
        return flow {
            val result = web3JService.ethSendRawTransaction(rawData).sendAsync()
                .get().transactionHash
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    override fun getAddress(isLegacy: Boolean): String {
        return hdWallet.getAddressForCoin(CoinType.ETHEREUM)
    }

    override fun validateAddress(address: String): Boolean {
        return AnyAddress.isValid(address, CoinType.ETHEREUM)
    }
}
