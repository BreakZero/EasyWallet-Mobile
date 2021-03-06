package com.easy.wallet.feature.transaction.adapter

import com.airbnb.epoxy.TypedEpoxyController
import com.easy.wallet.data.data.model.TransactionDataModel

class TransactionHistoryController(
  private val onItemClick: (transaction: TransactionDataModel) -> Unit
) : TypedEpoxyController<List<TransactionDataModel>>() {
  override fun buildModels(data: List<TransactionDataModel>?) {
    data?.forEach { transaction ->
      transaction {
        id(transaction.txHash)
        itemData(transaction)
        onItemClick {
          this@TransactionHistoryController.onItemClick.invoke(transaction)
        }
      }
    }
  }
}
