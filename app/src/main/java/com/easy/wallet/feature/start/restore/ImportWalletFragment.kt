package com.easy.wallet.feature.start.restore

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.core.widget.doAfterTextChanged
import com.easy.framework.base.BaseFragment
import com.easy.framework.delegate.viewBinding
import com.easy.wallet.R
import com.easy.wallet.databinding.FragmentStartImportBinding
import com.easy.wallet.feature.MainActivity
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import io.uniflow.android.livedata.onStates
import io.uniflow.core.flow.data.UIState
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent.getKoin

class ImportWalletFragment : BaseFragment(R.layout.fragment_start_import) {
  private val binding by viewBinding(FragmentStartImportBinding::bind)
  private val viewModel: ImportWalletViewModel by viewModel()

  override fun ownerToolbar(): MaterialToolbar? = null

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    getKoin().logger.debug("Open Fragment Scope: $scope")
  }

  @ExperimentalStdlibApi
  override fun setupView() {
    super.setupView()

    setTitle("Import Wallet")
    binding.edtImportWord.doAfterTextChanged {
      it?.let { editable ->
        if (it.endsWith(" ", true)
          and editable.isNotBlank()
        ) {
          addNewChip(editable.trim().toString(), binding.flexBox)
          editable.clear()
        }
      }
    }
    binding.edtImportWord.setOnKeyListener { _, keyCode, event ->
      if ((keyCode == KeyEvent.KEYCODE_DEL)
        and (event.action == KeyEvent.ACTION_UP)
        and (binding.flexBox.childCount >= 2)
      ) {
        viewModel.deleteLast()
        binding.flexBox.removeViewAt(binding.flexBox.childCount - 2)
      }
      false
    }

    binding.btnDone.setOnClickListener {
      viewModel.done()
    }
    binding.edtImportWord.requestFocus()

    onStates(viewModel) {
      when (it) {
        is UIState.Success -> {
          startActivity(Intent(requireActivity(), MainActivity::class.java))
          requireActivity().finish()
        }
        is UIState.Failed -> {
          Snackbar.make(binding.btnDone, "please try again", LENGTH_SHORT).show()
        }
      }
    }
  }

  private fun addNewChip(content: String, parent: FlexboxLayout) {
    val chip = Chip(requireContext()).apply {
      text = content
    }
    viewModel.addWord(content)
    parent.addView(chip as View, parent.childCount - 1)
  }
}
