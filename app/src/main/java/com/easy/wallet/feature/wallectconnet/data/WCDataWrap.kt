package com.easy.wallet.feature.wallectconnet.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WCDataWrap(
    val wcActionType: WCActionType,
    val methodId: Long,
    val orgIcon: String?,
    val orgName: String?,
    val orgUrl: String?,
    val resultData: String = "",
    val toAddress: String? = "",
    val gasLimit: String? = null,
    val ethValue: String? = null
) : Parcelable
