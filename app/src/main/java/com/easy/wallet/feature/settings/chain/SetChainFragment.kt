package com.easy.wallet.feature.settings.chain

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.easy.framework.base.BaseFragment
import com.easy.framework.delegate.viewBinding
import com.easy.wallet.R
import com.easy.wallet.data.WalletDataSDK
import com.easy.wallet.databinding.FragmentSettingsChainBinding
import com.easy.wallet.feature.settings.chain.adapter.ChainsController
import com.easy.wallet.feature.start.StartActivity
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.scope.requireScopeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

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
            WalletDataSDK.notifyChainChanged()
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
