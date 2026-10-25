package com.xorko.backlogmanager.modules.game.games.repositories

import com.github.benmanes.caffeine.cache.Caffeine
import com.xorko.backlogmanager.core.domain.logging.Logger
import com.xorko.backlogmanager.game.domain.models.Game
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.future.await
import kotlinx.coroutines.future.future
import java.time.Duration

class CachedGamesRepository(
    private val delegate: GamesRepository,
) : GamesRepository {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val gamesCache =
        Caffeine
            .newBuilder()
            .expireAfterWrite(Duration.ofHours(6))
            .maximumSize(200)
            .buildAsync<Pair<Int, Int>, List<Game>>()

    private val gameByIdCache =
        Caffeine
            .newBuilder()
            .expireAfterWrite(Duration.ofHours(6))
            .maximumSize(5_000)
            .buildAsync<Long, Game>()

    override suspend fun getGames(
        offset: Int,
        limit: Int,
    ): List<Game> =
        gamesCache
            .get(offset to limit) { _, _ ->
                Logger.d("Cache MISS for games with offset $offset and limit $limit. Forwarding request to delegate.")
                scope.future {
                    delegate.getGames(
                        offset = offset,
                        limit = limit,
                    )
                }
            }.await()

    override suspend fun getGameById(id: Long): Game =
        gameByIdCache
            .get(id) { _, _ ->
                Logger.d("Cache MISS for game with id $id. Forwarding request to delegate.")
                scope.future {
                    delegate.getGameById(id = id)
                }
            }.await()

    override suspend fun getGamesById(ids: List<Long>): List<Game> {
        if (ids.isEmpty()) return emptyList()

        val gamesById =
            gameByIdCache
                .getAll(ids.distinct()) { missingIds, _ ->
                    Logger.d("Cache MISS for games with ids ${ids.joinToString(", ")}. Forwarding request to delegate.")
                    scope.future {
                        delegate
                            .getGamesById(missingIds.toList())
                            .associateBy { it.id }
                    }
                }.await()

        return ids.map { gamesById.getValue(it) }
    }
}
