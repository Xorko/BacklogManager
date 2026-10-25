package com.xorko.backlogmanager.di

import androidx.room.RoomDatabase
import com.xorko.backlogmanager.game.data.local.database.BacklogDatabase
import com.xorko.backlogmanager.game.data.local.database.getBacklogDatabaseBuilder
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSURLCache

actual val platformModule: Module =
    module {
        single<HttpClientEngine> {
            Darwin.create {
                configureSession {
                    URLCache =
                        NSURLCache(
                            memoryCapacity = 5UL * 1024UL * 1024UL, // 5 MB
                            diskCapacity = 10UL * 1024UL * 1024UL, // 10 MB
                            diskPath = "http_cache",
                        )
                }
            }
        }
        single<RoomDatabase.Builder<BacklogDatabase>> { getBacklogDatabaseBuilder() }
    }
