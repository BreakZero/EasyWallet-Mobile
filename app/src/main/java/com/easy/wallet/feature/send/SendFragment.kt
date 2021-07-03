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
import com.easy.framework.ext.observeState
import com.easy.framework.ext.onSingleClick
import com.easy.wallet.R
import com.easy.wallet.databinding.FragmentSendBinding
import com.easy.wallet.databinding.IncludeSendAboutBinding
import com.easy.wallet.ext.start
import com.easy.wallet.feature.sharing.ScannerFragment
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.appbar.MaterialToolbar
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

    override fun setupView() {
        super.setupView()

        sendFormBinding = IncludeSendAboutBinding.bind(binding.root)
        binding.btnSendContinue.onSingleClick(lifecycleScope) {
            if (viewModel.isFullEnter()) {
                binding.btnSendContinue.isEnabled = false
                binding.btnSendContinue.showProgress {
                    buttonText = "building"
                    progressColor = Color.WHITE
                }

                viewModel.buildTransaction(binding.feeSlider.value, false) {
                    it?.let {
                        binding.btnSendContinue.isEnabled = true
                        binding.btnSendContinue.hideProgress(getString(R.string.text_continue))
                        start(
                            SendFragmentDirections.actionContinueToConfirm(
                                previewModel = it,
                                currencyInfo = args.currencyInfo
                            )
                        )
                    } ?: kotlin.run {
                        binding.btnSendContinue.isEnabled = true
                        binding.btnSendContinue.hideProgress(getString(R.string.text_continue))
                        Timber.d(getString(R.string.error_somethings_went_wrong))
                    }
                }
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

    override fun applyViewModel() {
        super.applyViewModel()
        viewModel.apply {
            loadBalance().observeState(
                owner = this@SendFragment,
                onSuccess = {
                    sendFormBinding.tvSendBalance.text = it
                },
                onError = {
                    sendFormBinding.tvSendBalance.text = "failed"
                },
                onLoading = {
                    sendFormBinding.tvSendBalance.text = "loading"
                }
            )
            initInfo().observe(
                this@SendFragment
            ) {
                setTitle("${it.symbol} Send")
                sendFormBinding.tvSendCoinName.text = it.name

                sendFormBinding.tvSendBalance.setTextColor(Color.parseColor(it.accentColor))

                binding.feeSlider.valueFrom = it.feeMin.toFloat()
                binding.feeSlider.valueTo = it.feeMax.toFloat()
                binding.feeSlider.value = it.feeMin.plus(8f)
            }
        }
    }
}