package com.easy.framework.ext

import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.delay

fun View.gone(shouldBeGone: Boolean) {
    visibility = if (shouldBeGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

fun View.onSingleClick(scope: CoroutineScope, action: (View) -> Unit) {
    val actor = scope.actor<View> {
        for (view in channel) {
            action.invoke(view)
            delay(800)
        }
    }

    setOnClickListener {
        actor.offer(it)
    }
}
