package com.xorko.backlogmanager.di

import com.xorko.backlogmanager.core.data.BaseHttpClientFactory
import com.xorko.backlogmanager.core.data.IgdbHttpClientFactory
import com.xorko.backlogmanager.core.data.logging.Slf4jLogger
import com.xorko.backlogmanager.core.domain.logging.AppLogger
import com.xorko.backlogmanager.di.Qualifiers.BASE_HTTP_CLIENT
import com.xorko.backlogmanager.di.Qualifiers.IGDB_HTTP_CLIENT
import com.xorko.backlogmanager.modules.game.di.gameModules
import com.xorko.backlogmanager.modules.igdb.proxy.di.igdbProxyModule
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val loggerModule =
    module {
        singleOf(::Slf4jLogger).bind(AppLogger::class)
    }

val coreModule =
    module {
        single<HttpClient>(named(BASE_HTTP_CLIENT)) { BaseHttpClientFactory.create() }
        single<HttpClient>(named(IGDB_HTTP_CLIENT)) { IgdbHttpClientFactory.create() }
    }

val appModules =
    listOf(
        loggerModule,
        coreModule,
        *gameModules,
        igdbProxyModule,
    )
