package com.easy.framework.common

import android.os.Build
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import timber.log.Timber

object RomUtils {
    private const val ROM_MIUI = "MIUI"
    private const val ROM_EMUI = "EMUI"
    private const val ROM_OPPO = "OPPO"
    private const val ROM_VIVO = "VIVO"

    private const val KEY_VERSION_MIUI = "ro.miui.ui.version.name"
    private const val KEY_VERSION_EMUI = "ro.build.version.emui"
    private const val KEY_VERSION_OPPO = "ro.build.version.opporom"
    private const val KEY_VERSION_VIVO = "ro.vivo.os.version"

    private var name: String? = null

    fun isMiui(): Boolean {
        return check(ROM_MIUI)
    }
    fun isEmui(): Boolean {
        return check(ROM_EMUI)
    }
    fun isVivo(): Boolean {
        return check(ROM_VIVO)
    }
    fun isOppo(): Boolean {
        return check(ROM_OPPO)
    }

    private fun check(rom: String): Boolean {
        return name?.let {
            it == rom
        } ?: kotlin.run {
            name = when {
                !getProp(KEY_VERSION_MIUI).isNullOrEmpty() -> {
                    ROM_MIUI
                }
                !getProp(KEY_VERSION_EMUI).isNullOrEmpty() -> {
                    ROM_EMUI
                }
                !getProp(KEY_VERSION_OPPO).isNullOrEmpty() -> {
                    ROM_OPPO
                }
                !getProp(KEY_VERSION_VIVO).isNullOrEmpty() -> {
                    ROM_VIVO
                }
                else -> Build.MANUFACTURER.uppercase()
            }
            name?.equals(rom) ?: false
        }
    }

    private fun getProp(name: String): String? {
        val line: String?
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $name")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            line = input.readLine()
            input.close()
        } catch (ex: IOException) {
            Timber.e("Unable to read prop $name")
            return null
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return line
    }
}