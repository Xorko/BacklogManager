package com.xorko.backlogmanager.modules.game.di

import com.xorko.backlogmanager.di.Qualifiers.IGDB_HTTP_CLIENT
import com.xorko.backlogmanager.modules.game.common.data.IgdbClient
import com.xorko.backlogmanager.modules.game.games.di.gamesModule
import com.xorko.backlogmanager.modules.game.popularity.di.popularityModule
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val commonModule =
    module {
        single { IgdbClient(get(named(IGDB_HTTP_CLIENT))) }
    }

val gameModules = arrayOf(commonModule, gamesModule, popularityModule)
