package com.easy.wallet.feature.sharing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import com.easy.wallet.R

class LoadingDialogFragment : AppCompatDialogFragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_loading, container, false)
      .apply {
        setBackgroundResource(R.drawable.background__white_12_radius)
      }
  }

  override fun getTheme(): Int {
    return R.style.Dialog_FullScreen
  }
}
