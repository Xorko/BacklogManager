package com.xorko.backlogmanager.di

import androidx.room.RoomDatabase
import com.xorko.backlogmanager.game.data.local.database.BacklogDatabase
import com.xorko.backlogmanager.game.data.local.database.getBacklogDatabaseBuilder
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module =
    module {
        single<HttpClientEngine> { Darwin.create() }
        single<RoomDatabase.Builder<BacklogDatabase>> { getBacklogDatabaseBuilder() }
    }
