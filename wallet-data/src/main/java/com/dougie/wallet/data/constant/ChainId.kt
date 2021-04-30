package com.dougie.wallet.data.constant

enum class ChainId(val id: Int) {
    MAINNET(1),
    ROPSTEN(3),
    RINKEBY(4),
    GÖRLI(5),
    KOVAN(42)
}

internal const val CHAINID_STORE_KEY = "chain-store-value"
