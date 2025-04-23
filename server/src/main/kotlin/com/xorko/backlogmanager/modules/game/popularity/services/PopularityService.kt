package com.xorko.backlogmanager.modules.game.popularity.services

import com.xorko.backlogmanager.modules.game.popularity.repositories.PopularityRepository

class PopularityService(
    private val popularityRepository: PopularityRepository,
) {
    suspend fun getMostPlayedGamesOnSteam(
        limit: Int,
        offset: Int,
    ) = popularityRepository.getMostPlayedGamesOnSteam(
        limit = limit,
        offset = offset,
    )
}
