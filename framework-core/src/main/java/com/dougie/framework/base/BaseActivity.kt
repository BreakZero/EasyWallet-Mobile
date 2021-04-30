package com.dougie.framework.base

import androidx.annotation.LayoutRes
import org.koin.androidx.scope.ScopeActivity

open class BaseActivity(@LayoutRes layoutRes: Int) : ScopeActivity(layoutRes) {
    protected fun setupView() {}
}
