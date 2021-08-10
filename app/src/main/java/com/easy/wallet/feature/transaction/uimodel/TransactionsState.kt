package com.easy.wallet.feature.transaction.uimodel

import com.easy.wallet.data.data.model.TransactionDataModel
import io.uniflow.core.flow.data.UIState

data class TransactionsState(
  val list: List<TransactionDataModel>
) : UIState()
