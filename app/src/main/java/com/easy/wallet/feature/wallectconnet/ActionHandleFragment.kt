package com.easy.wallet.feature.wallectconnet

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import com.easy.framework.base.BaseBottomSheetFragment
import com.easy.framework.delegate.viewBinding
import com.easy.framework.ext.onSingleClick
import com.easy.wallet.R
import com.easy.wallet.databinding.FragmentSwapBinding
import com.easy.wallet.feature.wallectconnet.data.WCDataWrap

class ActionHandleFragment : BaseBottomSheetFragment() {
  override fun layout(): Int = R.layout.fragment_swap

  private val binding by viewBinding(FragmentSwapBinding::bind)

  private val args: ActionHandleFragmentArgs by navArgs()

  override fun setupView() {
    super.setupView()
    val wcData = args.wrapData as WCDataWrap

    wcData.also {
      binding.tvWCActionName.text = it.wcActionType.name
      binding.tvWCOrgName.text = it.orgName
      it.orgUrl?.let { icon ->
        binding.ivWCIcon.load(icon) {
          crossfade(true)
          placeholder(R.drawable.ic_qr_code)
          transformations(CircleCropTransformation())
        }
      }
    }

    binding.btnWCPositive.onSingleClick(lifecycleScope) {
      WalletConnectService.approve(wcData.methodId)
      findNavController().navigateUp()
    }

    binding.btnWCNegative.onSingleClick(lifecycleScope) {
      WalletConnectService.reject(wcData.methodId)
      findNavController().navigateUp()
    }
  }
}
