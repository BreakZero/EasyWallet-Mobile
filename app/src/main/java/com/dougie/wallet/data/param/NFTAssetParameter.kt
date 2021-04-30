package com.dougie.wallet.data.param

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NFTAssetParameter(
    val contractAddress: String,
    val tokenId: String,
    val permalink: String
) : Parcelable
