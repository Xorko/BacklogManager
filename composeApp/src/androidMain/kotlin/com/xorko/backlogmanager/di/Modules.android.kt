package com.xorko.backlogmanager.di

import androidx.room.RoomDatabase
import com.xorko.backlogmanager.game.data.local.database.BacklogDatabase
import com.xorko.backlogmanager.game.data.local.database.getBacklogDatabaseBuilder
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.Cache
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module =
    module {
        single<HttpClientEngine> {
            OkHttp.create {
                config {
                    cache(
                        Cache(
                            directory = androidContext().cacheDir,
                            maxSize = 10L * 1024 * 1024, // 10 MB
                        ),
                    )
                }
            }
        }
        single<RoomDatabase.Builder<BacklogDatabase>> { getBacklogDatabaseBuilder(androidContext()) }
    }
