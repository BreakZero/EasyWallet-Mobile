package com.easy.wallet.data.provider

import com.easy.wallet.data.WalletDataSDK
import com.easy.wallet.data.data.model.SendModel
import com.easy.wallet.data.data.model.SendPlanModel
import com.easy.wallet.data.data.model.TransactionDataModel
import com.easy.wallet.data.error.InsufficientBalanceException
import com.google.protobuf.ByteString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import org.web3j.utils.Numeric
import wallet.core.java.AnySigner
import wallet.core.jni.AnyAddress
import wallet.core.jni.BitcoinScript
import wallet.core.jni.CoinType
import wallet.core.jni.proto.Bitcoin
import wallet.core.jni.proto.Common
import java.math.BigInteger

class BitcoinCashProvider : BaseProvider(WalletDataSDK.currWallet()) {
  companion object {
    private const val DECIMALS = 8
  }

  @ExperimentalUnsignedTypes
  override fun getBalance(address: String): Flow<BigInteger> {
    return flow {
      val balance = blockChairService.bitcoinRelatedInfoByAddress(
        "bitcoin-cash",
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
      val result = blockChairService.bitcoinRelatedInfoByAddress(
        "bitcoin-cash",
        address, limit, offset
      ).data.values.first().transactionsHash
      emit(result.joinToString(separator = ","))
    }.flatMapConcat { it ->
      flow {
        val wrapAddr = if (address.startsWith("bitcoincash")) {
          address.split(":").last()
        } else address
        val result = blockChairService.bitcoinTxsByHash("bitcoin-cash", it)
          .data.values.map {
            val isSend = wrapAddr.equals(it.inputs.first().recipient, true)
            TransactionDataModel.ofBitcoinType(
              it,
              wrapAddr,
              "BCH", DECIMALS, isSend
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
      val info = blockChairService.bitcoinRelatedInfoByAddress(
        "bitcoin-cash",
        from
      ).data.values.first()
      emit(info)
    }.map {
      if ((it.addressInfo.balance.toBigInteger() < sendAmount)
        and !sendModel.useMax
      ) throw InsufficientBalanceException()

      val hexScript = it.addressInfo.scriptHex
      val utxos = it.utxos
      val input = Bitcoin.SigningInput.newBuilder().apply {
        amount = sendAmount.toLong()
        hashType = BitcoinScript.hashTypeForCoin(CoinType.BITCOINCASH)
        toAddress = sendModel.to
        changeAddress = from
        byteFee = sendModel.feeByte.toLong()
        useMaxAmount = true
        coinType = CoinType.BITCOINCASH.value()
      }
      val prvData = hdWallet.getKeyForCoin(CoinType.BITCOINCASH)
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
            .setAmount(it.value)
            .setScript(script)
            .build()

        input.addUtxo(unspentTransaction)
      }
      val plan = AnySigner.plan(
        input.build(),
        CoinType.BITCOINCASH,
        Bitcoin.TransactionPlan.parser()
      )
      input.plan = plan
      val output = AnySigner.sign(
        input.build(),
        CoinType.BITCOINCASH,
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
      val result = blockChairService.pushTransaction(
        "bitcoin-cash",
        Numeric.cleanHexPrefix(rawData)
      ).data.txHash
      emit(result)
    }.flowOn(Dispatchers.IO)
  }

  override fun getAddress(isLegacy: Boolean): String {
    return hdWallet.getAddressForCoin(CoinType.BITCOINCASH)
  }

  override fun validateAddress(address: String): Boolean {
    return AnyAddress.isValid(address, CoinType.BITCOINCASH)
  }
}
