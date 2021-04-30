package com.dougie.wallet.data.provider

import com.dougie.wallet.data.DeFiWalletSDK
import com.dougie.wallet.data.constant.ChainId
import com.dougie.wallet.data.data.model.*
import com.dougie.wallet.data.error.NetworkError
import com.dougie.wallet.data.error.NotEthereumException
import com.dougie.wallet.data.error.UnSupportTokenException
import com.dougie.wallet.data.etx.toHexByteArray
import com.dougie.wallet.data.network.web3j.Web3JService
import com.google.protobuf.ByteString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.web3j.contracts.eip20.generated.ERC20
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.utils.Numeric
import wallet.core.java.AnySigner
import wallet.core.jni.AnyAddress
import wallet.core.jni.CoinType
import wallet.core.jni.proto.Ethereum
import java.math.BigDecimal
import java.math.BigInteger

object TokenAddress {
    private val tokenMap = hashMapOf<String, String>().apply {
        put("CRO-1", "0xa0b73e1ff0b80914ab6fe0444e65848c4c34450b")
        put("CRO-3", "0xa0b73e1ff0b80914ab6fe0444e65848c4c34450b")
        put("CRO-4", "0xa0b73e1ff0b80914ab6fe0444e65848c4c34450b")
        put("CRO-5", "0xa0b73e1ff0b80914ab6fe0444e65848c4c34450b")
        put("CRO-42", "0xa0b73e1ff0b80914ab6fe0444e65848c4c34450b")
        put("DAI-1", "0x6b175474e89094c44da98b954eedeac495271d0f")
        put("DAI-3", "0x6b175474e89094c44da98b954eedeac495271d0f")
        put("DAI-4", "0xc7ad46e0b8a400bb3c915120d284aafba8fc4735")
        put("DAI-5", "0xc7ad46e0b8a400bb3c915120d284aafba8fc4735")
        put("DAI-42", "0xc7ad46e0b8a400bb3c915120d284aafba8fc4735")
        put("COMP-1", "0xc00e94Cb662C3520282E6f5717214004A7f26888")
        put("COMP-3", "0xc00e94Cb662C3520282E6f5717214004A7f26888")
        put("COMP-4", "0xc00e94Cb662C3520282E6f5717214004A7f26888")
        put("COMP-5", "0xc00e94Cb662C3520282E6f5717214004A7f26888")
        put("COMP-42", "0xc00e94Cb662C3520282E6f5717214004A7f26888")
        put("UNI-1", "0x1f9840a85d5af5bf1d1762f925bdaddc4201f984")
        put("UNI-3", "0x1f9840a85d5af5bf1d1762f925bdaddc4201f984")
        put("UNI-4", "0x1f9840a85d5af5bf1d1762f925bdaddc4201f984")
        put("UNI-5", "0x1f9840a85d5af5bf1d1762f925bdaddc4201f984")
        put("UNI-42", "0x1f9840a85d5af5bf1d1762f925bdaddc4201f984")
        put("TCRO-1", "0xe0c395e598144a6a06c993d172455ffea6c04ad2")
        put("TCRO-3", "0xe0c395e598144a6a06c993d172455ffea6c04ad2")
        put("TCRO-4", "0xe0c395e598144a6a06c993d172455ffea6c04ad2")
        put("TCRO-5", "0xe0c395e598144a6a06c993d172455ffea6c04ad2")
        put("TCRO-42", "0xe0c395e598144a6a06c993d172455ffea6c04ad2")
    }

    fun address(symbol: String, chainId: ChainId) = tokenMap["$symbol-${chainId.id}"]
}

class ERC20Provider(
    symbol: String,
    private val decimals: Int = 8,
    private val nChainId: ChainId = ChainId.MAINNET
) : BaseProvider(DeFiWalletSDK.currWallet()) {
    companion object {
        private const val GAS_LIMIT = 50000
    }

    private val web3JService: Web3j = Web3JService.web3jClient(nChainId)
    private val contract: ERC20

    init {
        val address = TokenAddress.address(symbol, nChainId) ?: throw UnSupportTokenException()
        contract = ERC20.load(
            address,
            web3JService,
            Credentials.create(
                Numeric.toHexString(
                    hdWallet.getKeyForCoin(CoinType.ETHEREUM).data()
                )
            ),
            DefaultGasProvider()
        )
    }

    override fun getBalance(address: String): Flow<BigInteger> {
        return flow {
            val balance = contract.balanceOf(address).sendAsync().get()
            emit(balance)
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
                offset = offset,
                contractaddress = contract.contractAddress,
                action = "tokentx"
            )

            val result = response.result.map {
                val isSend = it.from == getAddress(false)
                TransactionDataModel(
                    txHash = it.hash,
                    time = it.timeStamp,
                    amount = it.value.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                    recipient = it.to,
                    sender = it.from,
                    status = TxStatus.CONFIRM,
                    direction = if (isSend) TxDirection.SEND else TxDirection.RECEIVE,
                    decimal = decimals,
                    symbol = it.tokenSymbol
                )
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    private suspend fun estimateGasLimitNeed(
        isContract: Boolean,
        toAddress: String,
        nonce: BigInteger,
        gasPrice: BigInteger,
        payload: String
    ): BigInteger {
        return if (isContract) "21000".toBigInteger()
        else web3JService.ethEstimateGas(
            Transaction.createContractTransaction(
                toAddress,
                nonce,
                gasPrice,
                payload
            )
        ).sendAsync().get().amountUsed
    }

    override fun buildTransactionPlan(sendModel: SendModel): Flow<SendPlanModel> {
        return flow {
            val balance = web3JService.ethGetBalance(
                getAddress(false),
                DefaultBlockParameterName.LATEST
            ).sendAsync().get().balance
            if (balance <= BigInteger.ZERO) throw NotEthereumException()
            val nonce = web3JService.ethGetTransactionCount(
                getAddress(false),
                DefaultBlockParameterName.LATEST
            ).sendAsync().get().transactionCount
            emit(nonce)
        }.map {
            it?.let {
                val prvKey = ByteString.copyFrom(hdWallet.getKeyForCoin(CoinType.ETHEREUM).data())
                val signingInput = Ethereum.SigningInput.newBuilder().apply {
                    privateKey = prvKey
                    toAddress = sendModel.to
                    chainId = ByteString.copyFrom(nChainId.id.toBigInteger().toByteArray())
                    nonce = ByteString.copyFrom((it).toHexByteArray())
                    gasPrice = ByteString.copyFrom(
                        sendModel.feeByte.toBigDecimal().movePointRight(9).toBigInteger()
                            .toHexByteArray()
                    )
                    gasLimit = ByteString.copyFrom(GAS_LIMIT.toHexByteArray())
                    transaction = Ethereum.Transaction.newBuilder().apply {
                        erc20Transfer = Ethereum.Transaction.ERC20Transfer.newBuilder().apply {
                            to = sendModel.to
                            amount = ByteString.copyFrom(
                                sendModel.amount.toHexByteArray()
                            )
                        }.build()
                    }.build()
                }

                val encoded = AnySigner.encode(signingInput.build(), CoinType.ETHEREUM)
                SendPlanModel(
                    amount = sendModel.amount.toBigDecimal().movePointLeft(decimals),
                    fromAddress = getAddress(false),
                    toAddress = sendModel.to,
                    gasLimit = GAS_LIMIT.toBigInteger(),
                    gas = sendModel.feeByte.toBigDecimal(),
                    feeDecimals = 9,
                    memo = sendModel.memo,
                    rawData = Numeric.toHexString(encoded)
                )
            } ?: kotlin.run {
                throw NetworkError()
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun broadcastTransaction(rawData: String): Flow<String> {
        return flow {
            val result = web3JService.ethSendRawTransaction(rawData)
                .sendAsync().get().transactionHash
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
