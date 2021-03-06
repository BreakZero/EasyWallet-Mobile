package com.easy.wallet.data.provider

import com.easy.wallet.data.data.model.SendModel
import com.easy.wallet.data.data.model.SendPlanModel
import com.easy.wallet.data.data.model.TransactionDataModel
import kotlinx.coroutines.flow.Flow
import java.math.BigInteger

interface IProvider {
  fun getBalance(address: String): Flow<BigInteger>
  fun getTransactions(
    address: String,
    limit: Int,
    offset: Int
  ): Flow<List<TransactionDataModel>>

  /**
   * @return raw data
   */
  fun buildTransactionPlan(sendModel: SendModel): Flow<SendPlanModel>

  /**
   * @param rawData
   * @return transaction hash
   */
  fun broadcastTransaction(rawData: String): Flow<String>

  fun getAddress(isLegacy: Boolean): String
  fun validateAddress(address: String): Boolean
}
