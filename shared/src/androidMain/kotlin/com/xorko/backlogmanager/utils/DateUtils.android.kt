package com.xorko.backlogmanager.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toInstant
import java.text.DateFormat
import java.util.Date
import java.util.Locale
import kotlin.time.Instant

private fun Instant.toLongDateFormat(): String {
    val formatter = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault())
    return formatter.format(Date(toEpochMilliseconds()))
}

actual fun LocalDateTime.toLongDateFormat(): String = toInstant(TimeZone.currentSystemDefault()).toLongDateFormat()

actual fun LocalDate.toLongDateFormat(): String = atStartOfDayIn(TimeZone.currentSystemDefault()).toLongDateFormat()
