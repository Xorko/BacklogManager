package com.xorko.backlogmanager.modules.game.games.di

import com.xorko.backlogmanager.di.Qualifiers
import com.xorko.backlogmanager.modules.game.games.repositories.CachedGamesRepository
import com.xorko.backlogmanager.modules.game.games.repositories.GamesRepository
import com.xorko.backlogmanager.modules.game.games.repositories.IgdbGamesRepository
import com.xorko.backlogmanager.modules.game.games.services.GamesService
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val gamesModule =
    module {
        singleOf(::IgdbGamesRepository) {
            named(Qualifiers.IGDB)
            bind<GamesRepository>()
        }
        single<GamesRepository> { CachedGamesRepository(delegate = get(named(Qualifiers.IGDB))) }
        singleOf(::GamesService)
    }
