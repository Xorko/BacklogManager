package com.xorko.backlogmanager.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private fun getFormatter() = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.getDefault())

actual fun LocalDateTime.toLongDateFormat(): String = getFormatter().format(this.toJavaLocalDateTime())

actual fun LocalDate.toLongDateFormat(): String = getFormatter().format(this.toJavaLocalDate())
