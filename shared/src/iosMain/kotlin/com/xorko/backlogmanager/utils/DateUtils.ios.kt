package com.xorko.backlogmanager.utils

import kotlinx.cinterop.BetaInteropApi
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDateFormatterLongStyle
import platform.Foundation.NSLocale
import platform.Foundation.create
import platform.Foundation.currentLocale

@OptIn(BetaInteropApi::class)
actual fun LocalDateTime.toLongDateFormat(): String {
    val instant = toInstant(TimeZone.currentSystemDefault())
    val nsDate = NSDate.create(timeIntervalSince1970 = instant.epochSeconds.toDouble())
    val formatter =
        NSDateFormatter().apply {
            locale = NSLocale.currentLocale()
            dateStyle = NSDateFormatterLongStyle
        }

    return formatter.stringFromDate(nsDate)
}
