package com.easy.wallet.feature.start.splash

import android.content.Intent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dougie.framework.base.BaseFragment
import com.dougie.framework.model.ResultStatus
import com.dougie.wallet.R
import com.dougie.wallet.feature.MainActivity
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension
import timber.log.Timber

@KoinApiExtension
class SplashFragment : BaseFragment(R.layout.fragment_splash) {
    private val viewModel by viewModel<SplashViewModel>()

    override fun ownerToolbar(): MaterialToolbar? = null

    override fun setupView() {
        super.setupView()

        lifecycleScope.launchWhenStarted {
            viewModel.importState().catch {
                Timber.e(it)
                toImport()
            }.collect {
                when (it) {
                    is ResultStatus.Success -> {
                        if (it.data) {
                            toMainScreen()
                        } else {
                            toImport()
                        }
                    }
                    is ResultStatus.Error -> {
                        toImport()
                    }
                    else -> {
                    }
                }
            }
        }
        viewModel.fetch()
    }

    private fun toImport() {
        findNavController().navigate(R.id.action_import)
    }

    private fun toMainScreen() {
        startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finish()
    }
}
