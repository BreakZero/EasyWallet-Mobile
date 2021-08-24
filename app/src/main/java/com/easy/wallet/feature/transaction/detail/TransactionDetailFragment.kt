package com.easy.wallet.feature.transaction.detail

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.easy.framework.base.BaseFragment
import com.easy.framework.delegate.viewBinding
import com.easy.framework.ext.onSingleClick
import com.easy.wallet.R
import com.easy.wallet.databinding.FragmentTransactionDetailBinding
import com.easy.wallet.ext.downDecimal
import com.google.android.material.appbar.MaterialToolbar

class TransactionDetailFragment : BaseFragment(R.layout.fragment_transaction_detail) {
  override fun ownerToolbar(): MaterialToolbar? = null

  private val binding by viewBinding(FragmentTransactionDetailBinding::bind)
  private val args: TransactionDetailFragmentArgs by navArgs()

  @SuppressLint("SetTextI18n")
  override fun setupView() {
    super.setupView()
    with(args.txModel) {
      binding.tvTxDetailStatus.text = status.toString()
      binding.tvTxDetailTime.text = time
      binding.valueAmount.text = "${amount.downDecimal(decimal, 8).toPlainString()} $symbol"
      binding.valueHash.text = txHash
      binding.valueToAddress.text = recipient
      binding.valueFromAddress.text = sender
      binding.valueFee.text = fee
    }
  }

  override fun initEvents() {
    binding.tvSeeMore.onSingleClick(lifecycleScope) {
      Toast.makeText(requireContext(), args.txModel.toString(), Toast.LENGTH_SHORT).show()
    }
  }
}
