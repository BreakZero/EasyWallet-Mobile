package com.easy.wallet.data.provider

import com.easy.wallet.data.WalletDataSDK
import com.easy.wallet.data.data.model.SendModel
import com.easy.wallet.data.data.model.SendPlanModel
import com.easy.wallet.data.data.model.TransactionDataModel
import com.easy.wallet.data.data.remote.terra.BaseTxModel
import com.easy.wallet.data.error.InsufficientBalanceException
import com.easy.wallet.data.network.terra.TerraClient
import com.easy.wallet.data.network.terra.TerraService
import com.google.protobuf.ByteString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import timber.log.Timber
import wallet.core.java.AnySigner
import wallet.core.jni.AnyAddress
import wallet.core.jni.CoinType
import wallet.core.jni.proto.Cosmos
import java.math.BigInteger

class TerraProvider : BaseProvider(WalletDataSDK.currWallet()) {
  companion object {
    private const val DECIMALS = 6
  }

  private val terraClient = TerraClient.client().create(TerraService::class.java)

  override fun getBalance(address: String): Flow<BigInteger> {
    return flow {
      val balance = terraClient.getBalance(
        address
      )
      emit(balance.balances.find {
        it.denom == "uluna"
      }?.amount?.toBigIntegerOrNull() ?: BigInteger.ZERO)
    }.catch {
      Timber.e(it)
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
    return flow {
      val result = terraClient.getBalance(getAddress(false))
      emit(result.balances.find {
        it.denom == "uluna"
      }?.amount?.toBigIntegerOrNull() ?: BigInteger.ZERO)
    }.flatMapConcat {
      if (it < sendModel.amount) throw InsufficientBalanceException()
      flow {
        val atomAccount = terraClient.getAccountInfo(getAddress(false))
        emit(atomAccount.account)
      }
    }.map {
      val prvKey = ByteString.copyFrom(hdWallet.getKeyForCoin(CoinType.TERRA).data())

      val txAmount = Cosmos.Amount.newBuilder().apply {
        amount = 200000000
        denom = "uluna"
      }.build()

      val sendCoinsMsg = Cosmos.Message.Send.newBuilder().apply {
        fromAddress = getAddress(false)
        toAddress = "terra1hdp298kaz0eezpgl6scsykxljrje3667d233ms"
        addAllAmounts(listOf(txAmount))
        typePrefix = "bank/MsgSend"
      }.build()

      val stakingMsg = Cosmos.Message.Delegate.newBuilder().apply {
        this.amount = txAmount
        this.delegatorAddress = getAddress(false)
        this.validatorAddress = "terravaloper1vk20anceu6h9s00d27pjlvslz3avetkvnwmr35"
        this.typePrefix = "staking/MsgDelegate"
      }.build()

//      val message = Cosmos.Message.newBuilder().apply {
//        sendCoinsMessage = sendCoinsMsg
//      }.build()
      val message = Cosmos.Message.newBuilder()
        .apply {
          this.stakeMessage = stakingMsg
        }.build()

      val feeAmount = Cosmos.Amount.newBuilder().apply {
        amount = 7000
        denom = "uluna"
      }.build()

      val cosmosFee = Cosmos.Fee.newBuilder().apply {
        gas = 600000
        addAllAmounts(listOf(feeAmount))
      }.build()

      val signingInput = Cosmos.SigningInput.newBuilder().apply {
        this.signingMode = Cosmos.SigningMode.Protobuf
        this.accountNumber = it.accountNumber.toLong()
        this.chainId = "bombay-12"
        this.memo = ""
        this.sequence = it.sequence.toLong()
        this.fee = cosmosFee
        this.privateKey = prvKey
        this.addAllMessages(listOf(message))
      }.build()

      val output = AnySigner.sign(signingInput, CoinType.TERRA, Cosmos.SigningOutput.parser())

      SendPlanModel(
        amount = sendModel.amount.toBigDecimal().movePointLeft(DECIMALS),
        fromAddress = getAddress(false),
        toAddress = sendModel.to,
        gas = 1000.toBigDecimal(),
        memo = sendModel.memo,
        feeDecimals = DECIMALS,
        rawData = output.serialized
      ).apply {
        Timber.d("===== $rawData")
      }
    }
  }

  override fun broadcastTransaction(rawData: String): Flow<String> {
    return flow {
      emit(
        terraClient.broadcastTransaction(
          BaseTxModel(
            txBytes = rawData,
            mode = "BROADCAST_MODE_SYNC"
          ).also {
            Timber.d("===== $it")
          }
        )
      )
    }.map {
      it.string()
    }
  }

  override fun getAddress(isLegacy: Boolean): String {
    return hdWallet.getAddressForCoin(CoinType.TERRA)
  }

  override fun validateAddress(address: String): Boolean {
    return AnyAddress.isValid(address, CoinType.TERRA)
  }
}
