package com.xorko.backlogmanager.di

import com.xorko.backlogmanager.core.data.HttpClientFactory
import com.xorko.backlogmanager.game.di.gameModule
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule =
    module {
        singleOf(HttpClientFactory::create)
    }

fun getSharedModules() =
    listOf(
        sharedModule,
        gameModule,
    )
