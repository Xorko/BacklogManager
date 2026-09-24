package com.xorko.backlogmanager.game.data.local

import com.xorko.backlogmanager.game.data.local.entities.BackloggedGameEntity
import kotlinx.coroutines.flow.Flow

interface LocalBacklogDataSource {
    fun getBackloggedGames(): Flow<List<BackloggedGameEntity>>

    fun isGameInBacklog(gameId: Long): Flow<Boolean>

    suspend fun getBackloggedGameById(gameId: Long): BackloggedGameEntity?

    suspend fun upsertBackloggedGame(entity: BackloggedGameEntity)

    suspend fun deleteBackloggedGame(gameId: Long)

    suspend fun getUnsyncedGames(): List<BackloggedGameEntity>
}
