package com.easy.wallet.feature.sharing.dapp

enum class DAppMethod {
    SIGNTRANSACTION,
    SIGNPERSONALMESSAGE,
    SIGNMESSAGE,
    SIGNTYPEDMESSAGE,
    ECRECOVER,
    REQUESTACCOUNTS,
    WATCHASSET,
    ADDETHEREUMCHAIN;

    companion object {
        fun fromValue(value: String): DAppMethod {
            return when (value) {
                "signTransaction" -> SIGNTRANSACTION
                "signPersonalMessage" -> SIGNPERSONALMESSAGE
                "signMessage" -> SIGNMESSAGE
                "signTypedMessage" -> SIGNTYPEDMESSAGE
                "ecRecover" -> ECRECOVER
                "requestAccounts" -> REQUESTACCOUNTS
                "watchAsset" -> WATCHASSET
                else -> ADDETHEREUMCHAIN
            }
        }
    }
}