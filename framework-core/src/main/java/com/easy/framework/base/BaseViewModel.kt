package com.easy.framework.base

import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    protected companion object {
        const val INIT_STATE = "state-initialed"
    }
}
