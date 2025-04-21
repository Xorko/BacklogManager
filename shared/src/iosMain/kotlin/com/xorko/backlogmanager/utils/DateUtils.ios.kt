package com.xorko.backlogmanager.utils

import kotlinx.cinterop.BetaInteropApi
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toInstant
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDateFormatterLongStyle
import platform.Foundation.NSLocale
import platform.Foundation.create
import platform.Foundation.currentLocale

@OptIn(BetaInteropApi::class)
private fun Instant.toLongDateFormat(): String {
    val nsDate = NSDate.create(timeIntervalSince1970 = epochSeconds.toDouble())
    val formatter =
        NSDateFormatter().apply {
            locale = NSLocale.currentLocale()
            dateStyle = NSDateFormatterLongStyle
        }

    return formatter.stringFromDate(nsDate)
}

actual fun LocalDateTime.toLongDateFormat() = toInstant(TimeZone.currentSystemDefault()).toLongDateFormat()

actual fun LocalDate.toLongDateFormat() = atStartOfDayIn(TimeZone.currentSystemDefault()).toLongDateFormat()
