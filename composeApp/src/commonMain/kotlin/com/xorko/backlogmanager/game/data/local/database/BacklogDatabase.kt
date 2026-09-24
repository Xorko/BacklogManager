package com.xorko.backlogmanager.game.data.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.xorko.backlogmanager.game.data.local.daos.BackloggedGameDao
import com.xorko.backlogmanager.game.data.local.entities.BackloggedGameEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(
    entities = [BackloggedGameEntity::class],
    version = 1,
    exportSchema = false,
)
@ConstructedBy(BacklogDatabaseConstructor::class)
abstract class BacklogDatabase : RoomDatabase() {
    abstract fun backloggedGameDao(): BackloggedGameDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object BacklogDatabaseConstructor : RoomDatabaseConstructor<BacklogDatabase> {
    override fun initialize(): BacklogDatabase
}

fun getBacklogDatabase(
    builder: RoomDatabase.Builder<BacklogDatabase>,
): BacklogDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
