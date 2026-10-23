package com.xorko.backlogmanager.game.data.local.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.xorko.backlogmanager.game.data.local.entities.BackloggedGameEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BackloggedGameDao {
    @Query("SELECT * FROM backlogged_games WHERE syncStatus != 'PENDING_DELETE' ORDER BY backloggedDate DESC")
    fun getBackloggedGames(): Flow<List<BackloggedGameEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM backlogged_games WHERE gameId = :gameId AND syncStatus != 'PENDING_DELETE')")
    fun isGameInBacklog(gameId: Long): Flow<Boolean>

    @Query("SELECT * FROM backlogged_games WHERE gameId = :gameId AND syncStatus != 'PENDING_DELETE' LIMIT 1")
    fun getBackloggedGameById(gameId: Long): Flow<BackloggedGameEntity?>

    @Upsert
    suspend fun upsertBackloggedGame(entity: BackloggedGameEntity)

    @Query("DELETE FROM backlogged_games WHERE gameId = :gameId")
    suspend fun deleteBackloggedGame(gameId: Long)

    @Query("SELECT * FROM backlogged_games WHERE syncStatus != 'SYNCED'")
    suspend fun getUnsyncedGames(): List<BackloggedGameEntity>
}
