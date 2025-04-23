package com.xorko.backlogmanager.modules.game.games.di

import com.xorko.backlogmanager.modules.game.games.repositories.GamesRepository
import com.xorko.backlogmanager.modules.game.games.repositories.IgdbGamesRepository
import com.xorko.backlogmanager.modules.game.games.services.GamesService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val gamesModule =
    module {
        singleOf(::IgdbGamesRepository).bind(GamesRepository::class)
        singleOf(::GamesService)
    }
