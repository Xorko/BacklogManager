package com.xorko.backlogmanager.game.di

import com.xorko.backlogmanager.game.data.network.KtorRemoteGameDataSource
import com.xorko.backlogmanager.game.data.network.RemoteGameDataSource
import com.xorko.backlogmanager.game.data.repositories.DefaultGameRepository
import com.xorko.backlogmanager.game.domain.GameRepository
import com.xorko.backlogmanager.game.presentation.backloggedgamelist.BackloggedGameListViewModel
import com.xorko.backlogmanager.game.presentation.gamelist.GameListViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val gameModule =
    module {
        singleOf(::KtorRemoteGameDataSource).bind<RemoteGameDataSource>()
        singleOf(::DefaultGameRepository).bind<GameRepository>()

        viewModelOf(::GameListViewModel)
        viewModelOf(::BackloggedGameListViewModel)
    }
