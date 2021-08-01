package com.easy.wallet.feature.start.splash

import android.content.Intent
import com.easy.framework.base.BaseFragment
import com.easy.wallet.R
import com.easy.wallet.ext.start
import com.easy.wallet.feature.MainActivity
import com.google.android.material.appbar.MaterialToolbar
import io.uniflow.android.livedata.onStates
import io.uniflow.core.flow.data.UIState
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : BaseFragment(R.layout.fragment_splash) {
    private val viewModel by viewModel<SplashViewModel>()

    override fun ownerToolbar(): MaterialToolbar? = null

    override fun setupView() {
        super.setupView()

        onStates(viewModel) {
            when (it) {
                is UIState.Success -> {
                    toMainScreen()
                }
                is UIState.Failed -> {
                    toImport()
                }
            }
        }
    }

    private fun toImport() {
        start(R.id.action_import)
    }

    private fun toMainScreen() {
        startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finish()
    }
}
