package com.xorko.backlogmanager.modules.game.popularity.repositories

import com.github.benmanes.caffeine.cache.Caffeine
import com.xorko.backlogmanager.core.domain.logging.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.future.await
import kotlinx.coroutines.future.future
import java.time.Duration

class CachedPopularityRepository(
    private val delegate: PopularityRepository,
) : PopularityRepository {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val gamesCache =
        Caffeine
            .newBuilder()
            .expireAfterWrite(Duration.ofHours(6))
            .maximumSize(200)
            .buildAsync<Pair<Int, Int>, List<Long>>()

    override suspend fun getMostPlayedGamesOnSteam(
        limit: Int,
        offset: Int,
    ): List<Long> =
        gamesCache
            .get(offset to limit) { _, _ ->
                Logger.d("Cache MISS for games with offset $offset and limit $limit. Forwarding request to delegate.")
                scope.future {
                    delegate.getMostPlayedGamesOnSteam(
                        offset = offset,
                        limit = limit,
                    )
                }
            }.await()
}
