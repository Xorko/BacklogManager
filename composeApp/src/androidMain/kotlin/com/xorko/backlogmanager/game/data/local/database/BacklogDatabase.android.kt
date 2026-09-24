package com.xorko.backlogmanager.game.data.local.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

private const val DATABASE_NAME = "backlog.db"

fun getBacklogDatabaseBuilder(context: Context): RoomDatabase.Builder<BacklogDatabase> {
    val dbFile = context.getDatabasePath(DATABASE_NAME)
    return Room.databaseBuilder<BacklogDatabase>(
        context = context.applicationContext,
        name = dbFile.absolutePath,
    )
}
