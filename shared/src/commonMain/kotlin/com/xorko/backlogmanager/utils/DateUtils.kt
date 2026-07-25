package com.xorko.backlogmanager.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

/**
 * Convert a unix timestamp to a [LocalDate].
 */
fun Long.toLocalDate(): LocalDate {
    val instant = Instant.fromEpochSeconds(this)
    return instant.toLocalDateTime(TimeZone.UTC).date
}

expect fun LocalDateTime.toLongDateFormat(): String

expect fun LocalDate.toLongDateFormat(): String
