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

private fun Instant.toDateFormat(dateFormat: Int): String {
    val formatter = DateFormat.getDateInstance(dateFormat, Locale.getDefault())
    return formatter.format(Date(toEpochMilliseconds()))
}

actual fun LocalDateTime.toShortDateFormat() = toInstant(TimeZone.currentSystemDefault()).toDateFormat(DateFormat.SHORT)

actual fun LocalDateTime.toMediumDateFormat() = toInstant(TimeZone.currentSystemDefault()).toDateFormat(DateFormat.MEDIUM)

actual fun LocalDateTime.toLongDateFormat(): String = toInstant(TimeZone.currentSystemDefault()).toDateFormat(DateFormat.LONG)

actual fun LocalDate.toLongDateFormat(): String = atStartOfDayIn(TimeZone.currentSystemDefault()).toDateFormat(DateFormat.LONG)
