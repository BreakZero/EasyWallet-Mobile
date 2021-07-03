package com.easy.wallet.data

import com.easy.framework.model.ResultStatus
import com.easy.wallet.data.provider.IProvider
import java.math.BigDecimal

data class Asset(
    val coinInfo: CurrencyInfo,
    val provider: IProvider,
    var balance: ResultStatus<BigDecimal>
)
