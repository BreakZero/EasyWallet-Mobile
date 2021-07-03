package com.easy.framework.common

import android.content.Context
import androidx.core.content.edit

class BasicStore(application: Context) {
    private val sharedPreferences =
        application.getSharedPreferences(BASIC_SP, Context.MODE_PRIVATE)

    companion object {
        private const val BASIC_SP = "basic-sp"
    }

    fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit {
            putBoolean(key, value)
        }
    }

    fun putString(key: String, value: String) {
        sharedPreferences.edit {
            putString(key, value)
        }
    }

    fun getBoolean(key: String, default: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, default)
    }

    fun getString(key: String, default: String = ""): String {
        return sharedPreferences.getString(key, default).orEmpty()
    }

    fun contains(key: String): Boolean {
        return sharedPreferences.contains(key)
    }

    fun remove(key: String) {
        sharedPreferences.edit {
            remove(key)
        }
    }

    fun clear() {
        sharedPreferences.edit {
            clear()
        }
    }
}
