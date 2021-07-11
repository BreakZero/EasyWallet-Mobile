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
        args.txModel.let {
            binding.tvTxDetailStatus.text = it.status.toString()
            binding.tvTxDetailTime.text = it.time
            binding.valueAmount.text =
                "${it.amount.downDecimal(it.decimal, 8).toPlainString()} ${it.symbol}"
            binding.valueHash.text = it.txHash
            binding.valueToAddress.text = it.recipient
            binding.valueFromAddress.text = it.sender
            binding.valueFee.text = it.fee
        }
    }

    override fun initEvents() {
        binding.tvSeeMore.onSingleClick(lifecycleScope) {
            Toast.makeText(requireContext(), args.txModel.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}
