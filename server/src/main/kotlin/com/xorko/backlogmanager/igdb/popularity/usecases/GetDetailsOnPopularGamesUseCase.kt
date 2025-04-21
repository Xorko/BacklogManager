package com.xorko.backlogmanager.igdb.popularity.usecases

import com.xorko.backlogmanager.game.data.dto.igdb.IgdbGameDto
import com.xorko.backlogmanager.igdb.games.services.IgdbGameService
import com.xorko.backlogmanager.igdb.popularity.services.IgdbPopularityService

class GetDetailsOnPopularGamesUseCase(
    private val igdbGameService: IgdbGameService,
    private val igdbPopularityService: IgdbPopularityService,
) {
    suspend operator fun invoke(
        limit: Int = 20,
        offset: Int = 0,
    ): List<IgdbGameDto> {
        val popularGamesIds =
            igdbPopularityService
                .getCurrentlyMostPlayedGamesOnSteam(
                    limit = limit,
                    offset = offset,
                ).map { it.id }

        return igdbGameService.getGamesByIds(popularGamesIds)
    }
}
