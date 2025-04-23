package com.xorko.backlogmanager.modules.game.popularity.repositories

interface PopularityRepository {
    suspend fun getMostPlayedGamesOnSteam(
        limit: Int,
        offset: Int,
    ): List<Long>
}
