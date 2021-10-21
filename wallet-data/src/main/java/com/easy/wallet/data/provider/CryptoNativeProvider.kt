package com.easy.wallet.data.provider

import com.easy.wallet.data.WalletDataSDK
import com.easy.wallet.data.data.model.SendModel
import com.easy.wallet.data.data.model.SendPlanModel
import com.easy.wallet.data.data.model.TransactionDataModel
import kotlinx.coroutines.flow.Flow
import wallet.core.jni.AnyAddress
import wallet.core.jni.CoinType
import java.math.BigInteger

class CryptoNativeProvider : BaseProvider(WalletDataSDK.currWallet()) {
  override fun getBalance(address: String): Flow<BigInteger> {
    TODO("Not yet implemented")
  }

  override fun getTransactions(
    address: String,
    limit: Int,
    offset: Int
  ): Flow<List<TransactionDataModel>> {
    TODO("Not yet implemented")
  }

  override fun buildTransactionPlan(sendModel: SendModel): Flow<SendPlanModel> {
    TODO("Not yet implemented")
  }

  override fun broadcastTransaction(rawData: String): Flow<String> {
    TODO("Not yet implemented")
  }

  override fun getAddress(isLegacy: Boolean): String {
    return hdWallet.getAddressForCoin(CoinType.CRYPTOORG)
  }

  override fun validateAddress(address: String): Boolean {
    return AnyAddress.isValid(address, CoinType.CRYPTOORG)
  }
}