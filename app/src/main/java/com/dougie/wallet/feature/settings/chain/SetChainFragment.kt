package com.dougie.wallet.feature.settings.chain

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dougie.framework.base.BaseFragment
import com.dougie.framework.delegate.viewBinding
import com.dougie.wallet.R
import com.dougie.wallet.data.DeFiWalletSDK
import com.dougie.wallet.databinding.FragmentSettingsChainBinding
import com.dougie.wallet.feature.settings.chain.adapter.ChainsController
import com.dougie.wallet.feature.start.StartActivity
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class SetChainFragment : BaseFragment(R.layout.fragment_settings_chain) {
    override fun ownerToolbar(): MaterialToolbar? = null

    private val binding by viewBinding(FragmentSettingsChainBinding::bind)
    private val viewModel by viewModel<SetChainViewModel>()

    private val chainsController by lazy {
        ChainsController {
            viewModel.updateState(it)
        }
    }

    private fun triggerRestart(context: AppCompatActivity) {
        Intent(context, StartActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(this)
            DeFiWalletSDK.notifyChainChanged()
            context.finishAffinity()
        }
    }

    override fun setupView() {
        super.setupView()
        inflateMenu(R.menu.menu_done) {
            viewModel.updateChain()
            lifecycleScope.launch {
                delay(1000L)
                triggerRestart(requireScopeActivity())
            }
        }

        binding.chainList.setController(chainsController)

        lifecycleScope.launchWhenStarted {
            viewModel.chains().collect {
                chainsController.setData(it)
            }
        }
    }
}
