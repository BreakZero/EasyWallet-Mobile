package com.easy.wallet.data

import com.dougie.framework.model.ResultStatus
import com.dougie.wallet.data.provider.IProvider
import java.math.BigDecimal

data class Asset(
    val coinInfo: CurrencyInfo,
    val provider: IProvider,
    var balance: ResultStatus<BigDecimal>
)
