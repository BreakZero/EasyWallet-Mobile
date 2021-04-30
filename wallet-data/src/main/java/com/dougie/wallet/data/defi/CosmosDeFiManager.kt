package com.dougie.wallet.data.defi

import com.dougie.wallet.data.network.cosmos.FigmentClient
import com.dougie.wallet.data.network.cosmos.FigmentService

class CosmosDeFiManager {
    internal val figmentClient = FigmentClient.client().create(FigmentService::class.java)

    fun delegate() {
    }

    fun reDelegate() {
    }

    fun withdraw() {
    }
}
