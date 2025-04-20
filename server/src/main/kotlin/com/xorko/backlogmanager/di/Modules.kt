package com.xorko.backlogmanager.di

import com.xorko.backlogmanager.core.data.logging.Slf4jLogger
import com.xorko.backlogmanager.core.domain.logging.AppLogger
import com.xorko.backlogmanager.igdb.di.igdbModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val loggerModule =
    module {
        singleOf(::Slf4jLogger) bind AppLogger::class
    }

val appModules =
    listOf(
        loggerModule,
        igdbModule,
    )
