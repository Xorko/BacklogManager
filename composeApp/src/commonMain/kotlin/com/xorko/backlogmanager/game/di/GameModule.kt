package com.xorko.backlogmanager.game.di

import com.xorko.backlogmanager.game.data.local.LocalBacklogDataSource
import com.xorko.backlogmanager.game.data.local.RoomLocalBacklogDataSource
import com.xorko.backlogmanager.game.data.local.database.BacklogDatabase
import com.xorko.backlogmanager.game.data.local.database.getBacklogDatabase
import com.xorko.backlogmanager.game.data.network.KtorRemoteGameDataSource
import com.xorko.backlogmanager.game.data.network.RemoteBacklogDataSource
import com.xorko.backlogmanager.game.data.network.RemoteGameDataSource
import com.xorko.backlogmanager.game.data.network.SupabaseRemoteBacklogDataSource
import com.xorko.backlogmanager.game.data.repositories.DefaultBacklogRepository
import com.xorko.backlogmanager.game.data.repositories.DefaultGameRepository
import com.xorko.backlogmanager.game.domain.BacklogRepository
import com.xorko.backlogmanager.game.domain.GameRepository
import com.xorko.backlogmanager.game.presentation.backloggedgamelist.BackloggedGameListViewModel
import com.xorko.backlogmanager.game.presentation.gamedetails.GameDetailsViewModel
import com.xorko.backlogmanager.game.presentation.gamelist.GameListViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val gameModule =
    module {
        single<BacklogDatabase> { getBacklogDatabase(get()) }
        single { get<BacklogDatabase>().backloggedGameDao() }

        singleOf(::RoomLocalBacklogDataSource).bind<LocalBacklogDataSource>()
        singleOf(::SupabaseRemoteBacklogDataSource).bind<RemoteBacklogDataSource>()
        singleOf(::DefaultBacklogRepository).bind<BacklogRepository>()

        singleOf(::KtorRemoteGameDataSource).bind<RemoteGameDataSource>()
        singleOf(::DefaultGameRepository).bind<GameRepository>()

        viewModelOf(::GameListViewModel)
        viewModelOf(::BackloggedGameListViewModel)
        viewModelOf(::GameDetailsViewModel)
    }
