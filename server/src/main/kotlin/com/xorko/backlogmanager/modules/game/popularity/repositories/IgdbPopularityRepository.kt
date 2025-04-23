package com.xorko.backlogmanager.modules.game.popularity.repositories

import com.xorko.backlogmanager.modules.game.common.data.IgdbClient

class IgdbPopularityRepository(
    private val igdbClient: IgdbClient,
) : PopularityRepository {
    override suspend fun getMostPlayedGamesOnSteam(
        limit: Int,
        offset: Int,
    ) = igdbClient.getMostPlayedGamesOnSteam(limit, offset).map { it.id }
}
