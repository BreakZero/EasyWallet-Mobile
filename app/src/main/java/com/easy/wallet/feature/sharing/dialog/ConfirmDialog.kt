package com.easy.wallet.feature.sharing.dialog

import android.os.Parcelable
import com.easy.framework.base.BaseDialogFragment
import com.easy.wallet.R
import kotlinx.parcelize.Parcelize

class ConfirmDialog: BaseDialogFragment() {
  override fun layout(): Int = R.layout.dialog_confirm_normal
}

@Parcelize
data class ConfirmMessage(
  val title: String,
  val negativeButton: String,
  val positiveButton: String,
  val message: String
): Parcelable