package com.xorko.backlogmanager.game.data.local

import com.xorko.backlogmanager.game.data.local.daos.BackloggedGameDao
import com.xorko.backlogmanager.game.data.local.entities.BackloggedGameEntity
import kotlinx.coroutines.flow.Flow

class RoomLocalBacklogDataSource(
    private val dao: BackloggedGameDao,
) : LocalBacklogDataSource {
    override fun getBackloggedGames(): Flow<List<BackloggedGameEntity>> = dao.getBackloggedGames()

    override fun isGameInBacklog(gameId: Long): Flow<Boolean> = dao.isGameInBacklog(gameId)

    override fun getBackloggedGameById(gameId: Long): Flow<BackloggedGameEntity?> = dao.getBackloggedGameById(gameId)

    override suspend fun upsertBackloggedGame(entity: BackloggedGameEntity) = dao.upsertBackloggedGame(entity)

    override suspend fun deleteBackloggedGame(gameId: Long) = dao.deleteBackloggedGame(gameId)

    override suspend fun getUnsyncedGames(): List<BackloggedGameEntity> = dao.getUnsyncedGames()
}
