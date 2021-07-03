package com.easy.framework.common

import android.os.Handler
import android.os.Looper
import timber.log.Timber

object CrashCollection {
    fun handleCrash(callback: () -> Unit = {}) {
        Handler(Looper.getMainLooper()).post {
            while (true) {
                kotlin.runCatching {
                    Looper.loop()
                }.onFailure {
                    Timber.e(it)
                    Timber.e("Thread Name: Main, Error Message: ${it.message}")
                    callback.invoke()
                }
            }
        }
        Thread.setDefaultUncaughtExceptionHandler { thread, ex ->
            Timber.e(ex)
            Timber.e("Thread Name: ${thread.name}, Error Message: ${ex.message}")
            callback.invoke()
        }
    }

    private fun collectCrashInfo() {
        // post message to backend or store error message into local file
    }
}
