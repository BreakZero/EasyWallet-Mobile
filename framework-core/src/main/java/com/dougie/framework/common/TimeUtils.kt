package com.dougie.framework.common

import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

object TimeUtils {
    private val DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss")

    fun timestampToString(
        timestamp: Long,
        formatter: DateTimeFormatter = DEFAULT_FORMATTER
    ): String {
        return Instant.ofEpochSecond(timestamp)
            .atZone(ZoneId.systemDefault()).format(formatter)
    }
}
