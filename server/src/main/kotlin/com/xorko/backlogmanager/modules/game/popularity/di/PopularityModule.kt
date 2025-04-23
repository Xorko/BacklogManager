package com.xorko.backlogmanager.modules.game.popularity.di

import com.xorko.backlogmanager.modules.game.popularity.repositories.IgdbPopularityRepository
import com.xorko.backlogmanager.modules.game.popularity.repositories.PopularityRepository
import com.xorko.backlogmanager.modules.game.popularity.services.PopularityService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val popularityModule =
    module {
        singleOf(::IgdbPopularityRepository).bind(PopularityRepository::class)
        singleOf(::PopularityService)
    }
