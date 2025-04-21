package com.xorko.backlogmanager.igdb.di

import com.xorko.backlogmanager.igdb.games.services.IgdbGameService
import com.xorko.backlogmanager.igdb.popularity.services.IgdbPopularityService
import com.xorko.backlogmanager.igdb.popularity.usecases.GetDetailsOnPopularGamesUseCase
import com.xorko.backlogmanager.igdb.proxy.services.IgdbProxyService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val igdbModule =
    module {
        singleOf(::IgdbProxyService)
        singleOf(::IgdbGameService)
        singleOf(::IgdbPopularityService)
        singleOf(::GetDetailsOnPopularGamesUseCase)
    }
