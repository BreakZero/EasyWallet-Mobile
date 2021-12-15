package com.easy.wallet.feature

import android.Manifest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import com.easy.framework.base.BaseFragment
import com.easy.framework.delegate.viewBinding
import com.easy.framework.ext.onSingleClick
import com.easy.wallet.R
import com.easy.wallet.databinding.FragmentTestBinding
import com.easy.wallet.ext.start
import com.easy.wallet.feature.sharing.ScannerFragment
import com.google.android.material.appbar.MaterialToolbar
import org.walletconnect.walletconnectv2.WalletConnectClient
import org.walletconnect.walletconnectv2.client.ClientTypes
import timber.log.Timber

class TestFragment : BaseFragment(R.layout.fragment_test) {
  override fun ownerToolbar(): MaterialToolbar? = null
  private val askCameraPermission =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
      if (result) {
        start(R.id.global_action_scan)
      } else {
        Timber.d("camera permission denied")
      }
    }
  private val binding by viewBinding(FragmentTestBinding::bind)
  override fun setupView() {
    super.setupView()

    binding.btnConnect.onSingleClick(lifecycleScope) {
      askCameraPermission.launch(Manifest.permission.CAMERA)
    }

    setFragmentResultListener(ScannerFragment.REQUEST_QR_CODE) { _, bundle ->
      val result = bundle.getString(ScannerFragment.KEY_QR_CODE)
      val pair = ClientTypes.PairParams(result.orEmpty())
      WalletConnectClient.pair(pair) { sessionProposal ->
        Timber.d("====== $sessionProposal")
        val proposerPublicKey: String = sessionProposal.proposerPublicKey
        val proposalTtl: Long = sessionProposal.ttl
        val proposalTopic: String = sessionProposal.topic
        val accounts = sessionProposal.chains.map { chainId ->
          "$chainId:0x022c0c42a80bd19EA4cF0F94c4F9F96645759716"
        }
        val approveParams: ClientTypes.ApproveParams =
          ClientTypes.ApproveParams(accounts, proposerPublicKey, proposalTtl, proposalTopic)

        WalletConnectClient.approve(approveParams)
      }
    }
  }
}