package com.xorko.backlogmanager.core.di

import com.xorko.backlogmanager.core.data.HttpClientFactory
import com.xorko.backlogmanager.core.data.logging.KermitAppLogger
import com.xorko.backlogmanager.core.domain.logging.AppLogger
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreModule =
    module {
        single<AppLogger> { KermitAppLogger }
        singleOf(HttpClientFactory::create)
    }
