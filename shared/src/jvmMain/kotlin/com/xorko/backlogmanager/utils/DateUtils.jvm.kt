package com.xorko.backlogmanager.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import java.text.DateFormat
import java.util.Date
import java.util.Locale

actual fun LocalDateTime.toLongDateFormat(): String {
    val formatter = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault())
    val instant = toInstant(TimeZone.currentSystemDefault())

    return formatter.format(Date(instant.toEpochMilliseconds()))
}
