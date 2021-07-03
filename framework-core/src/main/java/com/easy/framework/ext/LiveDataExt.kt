package com.easy.framework.ext

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.dougie.framework.common.EventLiveData
import com.dougie.framework.common.StatefulLiveData
import com.dougie.framework.model.Event
import com.dougie.framework.model.RequestState

@MainThread
inline fun <T> EventLiveData<T>.observeEvent(
    owner: LifecycleOwner,
    crossinline onChanged: (T) -> Unit
): Observer<Event<T>> {
    val wrappedObserver = Observer<Event<T>> {
        it.getContentIfNotHandled()?.let { data ->
            onChanged.invoke(data)
        }
    }
    observe(owner, wrappedObserver)
    return wrappedObserver
}

@MainThread
inline fun <T> StatefulLiveData<T>.observeState(
    owner: LifecycleOwner,
    crossinline onLoading: () -> Unit = {},
    crossinline onSuccess: (T) -> Unit = {},
    crossinline onError: (Throwable?) -> Unit = {}
) {
    observe(
        owner,
        Observer { state ->
            when (state) {
                is RequestState.Loading -> onLoading.invoke()
                is RequestState.Success -> onSuccess.invoke(state.data)
                is RequestState.Error -> onError.invoke(state.error)
            }
        }
    )
}
