package com.easy.wallet.data

import com.easy.framework.model.ResultStatus
import com.easy.wallet.data.provider.IProvider
import io.uniflow.core.flow.data.UIState
import java.math.BigDecimal

data class Asset(
    val coinInfo: CurrencyInfo,
    val provider: IProvider,
    var balance: BigDecimal? = null
)

data class AssetListState(
    val assets: List<Asset>
) : UIState()
