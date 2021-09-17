package com.easy.wallet.feature.sharing.dapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DAppConnectInfo(
  val chainId: Int,
  val rpc: String,
  val appUrl: String
) : Parcelable
