package com.easy.wallet.feature.defi

import androidx.lifecycle.viewModelScope
import io.uniflow.android.AndroidDataFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class TestDefiViewModel : AndroidDataFlow() {
  private val state = MutableStateFlow(0)

  fun state() = state

  fun observe() {
    flow {
      kotlinx.coroutines.delay(2000)
      emit(1)
    }.onEach {
      state.value = it
    }.launchIn(viewModelScope)
  }
}
