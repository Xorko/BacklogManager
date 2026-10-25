package com.xorko.backlogmanager.modules.game.popularity.di

import com.xorko.backlogmanager.di.Qualifiers
import com.xorko.backlogmanager.modules.game.popularity.repositories.CachedPopularityRepository
import com.xorko.backlogmanager.modules.game.popularity.repositories.IgdbPopularityRepository
import com.xorko.backlogmanager.modules.game.popularity.repositories.PopularityRepository
import com.xorko.backlogmanager.modules.game.popularity.services.PopularityService
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val popularityModule =
    module {
        singleOf(::IgdbPopularityRepository) {
            named(Qualifiers.IGDB)
            bind<PopularityRepository>()
        }
        single<PopularityRepository> { CachedPopularityRepository(delegate = get(named(Qualifiers.IGDB))) }
        singleOf(::PopularityService)
    }
