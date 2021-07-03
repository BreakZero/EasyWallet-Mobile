package com.easy.framework.base

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.easy.framework.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.transition.platform.MaterialElevationScale
import org.koin.androidx.scope.ScopeFragment

abstract class BaseFragment(@LayoutRes layoutRes: Int) : ScopeFragment(layoutRes) {
    private var gToolbar: MaterialToolbar? = null

    abstract fun ownerToolbar(): MaterialToolbar?

    protected fun updateStatusBarColor(@ColorInt color: Int) {
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        requireActivity().window.statusBarColor = color
        requireActivity().window.navigationBarColor = color

        if (isLightColor(color)) {
            requireActivity().window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            gToolbar?.navigationIcon?.colorFilter = PorterDuffColorFilter(
                ContextCompat.getColor(requireContext(), android.R.color.black),
                PorterDuff.Mode.SRC
            )
        } else {
            requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            gToolbar?.navigationIcon?.colorFilter = PorterDuffColorFilter(
                ContextCompat.getColor(requireContext(), android.R.color.white),
                PorterDuff.Mode.SRC
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialElevationScale(/* growing= */ true)
    }

    private fun isLightColor(@ColorInt color: Int): Boolean {
        return ColorUtils.calculateLuminance(color) >= 0.5
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateStatusBarColor(Color.parseColor("#FF3700B3"))

        bindToolbar()
        applyViewModel()
        setupView()
        initEvents()
    }

    private fun bindToolbar() {
        val navController = findNavController()

        gToolbar = ownerToolbar() ?: view?.findViewById(R.id.bToolbar)
        gToolbar?.let {
            it.setupWithNavController(navController)
            it.navigationIcon?.colorFilter = PorterDuffColorFilter(
                ContextCompat.getColor(requireContext(), R.color.md_white_1000),
                PorterDuff.Mode.SRC
            )
        }
    }

    protected fun setTitle(title: String) {
        gToolbar?.title = title
    }

    protected open fun inflateMenu(menuId: Int, callback: (id: Int) -> Unit) {
        gToolbar?.menu?.clear()
        gToolbar?.inflateMenu(menuId)
        gToolbar?.setOnMenuItemClickListener {
            callback.invoke(it.itemId)
            return@setOnMenuItemClickListener true
        }
    }

    protected open fun setupView() {}
    protected open fun initEvents() {}
    protected open fun applyViewModel() {}
}
