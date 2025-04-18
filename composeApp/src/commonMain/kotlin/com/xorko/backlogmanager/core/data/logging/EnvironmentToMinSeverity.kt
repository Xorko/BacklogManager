package com.xorko.backlogmanager.core.data.logging

import co.touchlab.kermit.Severity.Info
import co.touchlab.kermit.Severity.Verbose
import co.touchlab.kermit.Severity.Warn
import com.xorko.backlogmanager.core.data.configuration.Environment
import com.xorko.backlogmanager.core.data.configuration.Environment.DEV
import com.xorko.backlogmanager.core.data.configuration.Environment.PRODUCTION
import com.xorko.backlogmanager.core.data.configuration.Environment.STAGING

fun Environment.toMinSeverity() =
    when (this) {
        DEV -> Verbose
        STAGING -> Info
        PRODUCTION -> Warn
    }
