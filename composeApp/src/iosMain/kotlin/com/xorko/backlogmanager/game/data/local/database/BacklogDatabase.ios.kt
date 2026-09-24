package com.xorko.backlogmanager.game.data.local.database

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
fun getBacklogDatabaseBuilder(): RoomDatabase.Builder<BacklogDatabase> {
    val documentDirectory =
        NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = true,
            error = null,
        )
    val dbFile = requireNotNull(documentDirectory?.path) + "/backlog.db"
    return Room.databaseBuilder<BacklogDatabase>(
        name = dbFile,
    )
}
