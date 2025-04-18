package com.xorko.backlogmanager.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Convert a unix timestamp to a [LocalDate].
 */
fun Long.toLocalDate(): LocalDate {
    val instant = Instant.fromEpochSeconds(this)
    return instant.toLocalDateTime(TimeZone.UTC).date
}
