package com.xorko.backlogmanager.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

private fun getDateFormatter(formatStyle: FormatStyle) = DateTimeFormatter.ofLocalizedDate(formatStyle).withLocale(Locale.getDefault())

private fun LocalDateTime.format(formatStyle: FormatStyle): String {
    val formatter = getDateFormatter(formatStyle)
    return formatter.format(toJavaLocalDateTime())
}

private fun LocalDate.format(formatStyle: FormatStyle): String {
    val formatter = getDateFormatter(formatStyle)
    return formatter.format(toJavaLocalDate())
}

actual fun LocalDateTime.toShortDateFormat(): String = format(FormatStyle.SHORT)

actual fun LocalDateTime.toMediumDateFormat(): String = format(FormatStyle.MEDIUM)

actual fun LocalDateTime.toLongDateFormat(): String = format(FormatStyle.LONG)

actual fun LocalDate.toLongDateFormat(): String = format(FormatStyle.LONG)
