package com.easy.wallet.feature.send

import android.Manifest
import android.graphics.Color
import android.icu.text.NumberFormat
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.easy.framework.base.BaseFragment
import com.easy.framework.delegate.viewBinding
import com.easy.framework.ext.onSingleClick
import com.easy.wallet.R
import com.easy.wallet.data.CurrencyInfo
import com.easy.wallet.databinding.FragmentSendBinding
import com.easy.wallet.databinding.IncludeSendAboutBinding
import com.easy.wallet.ext.start
import com.easy.wallet.feature.send.uimodel.SendStates
import com.easy.wallet.feature.send.uimodel.SendUIEvents
import com.easy.wallet.feature.sharing.ScannerFragment
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.appbar.MaterialToolbar
import io.uniflow.android.livedata.onEvents
import io.uniflow.android.livedata.onStates
import io.uniflow.core.flow.data.UIState
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class SendFragment : BaseFragment(R.layout.fragment_send) {
    private val args: SendFragmentArgs by navArgs()

    private val format = NumberFormat.getInstance().apply {
        maximumFractionDigits = 0
    }
    private val binding by viewBinding(FragmentSendBinding::bind)
    private lateinit var sendFormBinding: IncludeSendAboutBinding

    private val viewModel: SendViewModel by stateViewModel {
        parametersOf(args.currencyInfo)
    }

    private val askCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                start(R.id.global_action_scan)
            } else {
                Timber.d("camera permission denied")
            }
        }

    override fun ownerToolbar(): MaterialToolbar? = null

    override fun initEvents() {
        onEvents(viewModel) {
            when (it) {
                SendUIEvents.ToSend -> {
                    binding.btnSendContinue.isEnabled = false
                    binding.btnSendContinue.showProgress {
                        buttonText = "building"
                        progressColor = Color.WHITE
                    }
                }
                SendUIEvents.BuildError -> {
                    binding.btnSendContinue.isEnabled = true
                    binding.btnSendContinue.hideProgress(getString(R.string.text_try_again))
                }
            }
        }
        onStates(viewModel) {
            when (it) {
                is SendStates.BuildState -> {
                    binding.btnSendContinue.isEnabled = true
                    binding.btnSendContinue.hideProgress(getString(R.string.text_continue))
                    start(
                        SendFragmentDirections.actionContinueToConfirm(
                            previewModel = it.sendModel,
                            currencyInfo = args.currencyInfo
                        )
                    )
                }
                is SendStates.InfoState -> {
                    val info = it.currency
                    renderInfo(info)

                    sendFormBinding.tvSendBalance.text = "${it.balance} ${info.symbol}"
                }
                is SendStates.BalanceFailedState -> {
                    renderInfo(it.currency)
                    sendFormBinding.tvSendBalance.text = "failed"
                }
                is UIState.Loading -> {
                    // also show loading view here. just do what you want to do.
                    sendFormBinding.tvSendBalance.text = "loading"
                }
            }
        }
    }

    private fun renderInfo(info: CurrencyInfo) {
        setTitle("${info.symbol} Send")
        sendFormBinding.tvSendCoinName.text = info.name

        sendFormBinding.tvSendBalance.setTextColor(Color.parseColor(info.accentColor))

        binding.feeSlider.valueFrom = info.feeMin.toFloat()
        binding.feeSlider.valueTo = info.feeMax.toFloat()
        binding.feeSlider.value = info.feeMin.plus(8f)
    }

    override fun setupView() {
        super.setupView()

        sendFormBinding = IncludeSendAboutBinding.bind(binding.root)
        binding.btnSendContinue.onSingleClick(lifecycleScope) {
            if (viewModel.isFullEnter()) {
                viewModel.buildTransaction(binding.feeSlider.value, false)
            }
        }

        sendFormBinding.inputAddress.setEndIconOnClickListener {
            askCameraPermission.launch(Manifest.permission.CAMERA)
        }

        binding.feeSlider.setLabelFormatter {
            format.format(it.toDouble())
        }

        binding.feeSlider.addOnChangeListener { _, value, _ ->
            binding.tvFeeByte.text = "${format.format(value.toDouble())} ${viewModel.feeUnit()}"
        }

        sendFormBinding.edtSendAddress.doOnTextChanged { text, _, _, _ ->
            viewModel.updateEnter(ModelFieldKey.ADDRESS, text.toString())
        }

        sendFormBinding.edtSendAmount.doOnTextChanged { text, _, _, _ ->
            viewModel.updateEnter(ModelFieldKey.AMOUNT, text.toString())
        }

        sendFormBinding.edtSendMemo.doOnTextChanged { text, _, _, _ ->
            viewModel.updateEnter(ModelFieldKey.MEMO, text.toString())
        }

        setFragmentResultListener(ScannerFragment.REQUEST_QR_CODE) { _, bundle ->
            val result = bundle.getString(ScannerFragment.KEY_QR_CODE)
            sendFormBinding.edtSendAddress.setText(result)
        }
    }
}
