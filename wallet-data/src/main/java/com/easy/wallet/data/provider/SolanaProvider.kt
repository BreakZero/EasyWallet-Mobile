package com.easy.wallet.data.provider

import com.easy.wallet.data.WalletDataSDK
import com.easy.wallet.data.data.model.SendModel
import com.easy.wallet.data.data.model.SendPlanModel
import com.easy.wallet.data.data.model.TransactionDataModel
import com.easy.wallet.data.data.remote.rpc.BaseRPCReq
import com.easy.wallet.data.network.solana.SolanaClient
import com.easy.wallet.data.network.solana.SolanaService
import com.google.protobuf.ByteString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import wallet.core.java.AnySigner
import wallet.core.jni.CoinType
import wallet.core.jni.proto.Solana
import java.math.BigInteger

class SolanaProvider : BaseProvider(WalletDataSDK.currWallet()) {

  private val solClient = SolanaClient.client().create(SolanaService::class.java)

  override fun getBalance(address: String): Flow<BigInteger> {
    return flow {
      val reqBody = BaseRPCReq(
        jsonrpc = "2.0",
        id = 1,
        method = "getBalance",
        params = listOf(address)
      )
      val resp =
        solClient.getBalance(reqBody = reqBody)
      emit(resp.result.value.toBigInteger())
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
    return flow {
      val reqBody = BaseRPCReq(
        jsonrpc = "2.0",
        id = 1,
        method = "getRecentBlockhash",
        params = emptyList<String>()
      )
      val result = solClient.recentBlockHash(reqBody = reqBody)
      emit(result.result.value.blockhash)
    }.flatMapConcat { blockHash ->
      val prvKey = ByteString.copyFrom(hdWallet.getKeyForCoin(CoinType.SOLANA).data())
      val transferMessage = Solana.Transfer.newBuilder().apply {
        recipient = sendModel.to
        value = sendModel.amount.toLong()
      }.build()
      val signingInput = Solana.SigningInput.newBuilder().apply {
        transferTransaction = transferMessage
        recentBlockhash = blockHash
        privateKey = prvKey
      }.build()

      val output = AnySigner.sign(signingInput, CoinType.SOLANA, Solana.SigningOutput.parser())
      val rawData = output.encoded

      flow {
        emit(
          SendPlanModel(
            amount = sendModel.amount.toBigDecimal().movePointLeft(9),
            fromAddress = getAddress(false),
            toAddress = sendModel.to,
            gasLimit = BigInteger.ONE,
            gas = sendModel.feeByte.toBigDecimal(),
            feeDecimals = 9,
            memo = sendModel.memo,
            rawData = rawData
          )
        )
      }
    }
  }

  override fun broadcastTransaction(rawData: String): Flow<String> {
    return flow {
      val reqBody = BaseRPCReq(
        jsonrpc = "2.0",
        id = 1,
        method = "sendTransaction",
        params = listOf(rawData)
      )
      val result = solClient.broadcastTransaction(reqBody = reqBody)
      emit(result.result)
    }
  }

  override fun getAddress(isLegacy: Boolean): String {
    return hdWallet.getAddressForCoin(CoinType.SOLANA)
  }

  override fun validateAddress(address: String): Boolean {
    return CoinType.SOLANA.validate(address)
  }
}
