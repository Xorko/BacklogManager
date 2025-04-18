package com.xorko.backlogmanager.di

import com.xorko.backlogmanager.core.di.coreModule
import com.xorko.backlogmanager.game.di.gameModule
import org.koin.core.module.Module

expect val platformModule: Module

fun getSharedModules() =
    listOf(
        coreModule,
        gameModule,
    )
