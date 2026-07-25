package com.xorko.backlogmanager.game.data.utils

object IgdbQueryFactory {
    private val commonFields =
        arrayOf(
            "id",
            "name",
            "summary",
            "genres.name",
            "first_release_date",
            "platforms.name",
            "rating",
            "rating_count",
            "aggregated_rating",
            "aggregated_rating_count",
            "cover.image_id",
            "artworks.image_id",
            "involved_companies.company.name",
            "involved_companies.developer",
            "involved_companies.publisher",
            "involved_companies.porting",
            "involved_companies.supporting",
        )

    private val onlyGamesWhereClause =
        WhereClause.and(
            WhereClause.condition("version_parent = null"),
            WhereClause.condition("game_type = (0,8)"),
        )

    fun searchGames(
        query: String,
        limit: Int? = null,
        offset: Int? = null,
    ): String {
        val builder =
            IgdbQueryBuilder()
                .search(query)
                .fields(*commonFields)
                .where(onlyGamesWhereClause)
        limit?.let { builder.limit(it) }
        offset?.let { builder.offset(it) }

        return builder.build()
    }

    fun getGames(
        limit: Int? = null,
        offset: Int? = null,
    ): String {
        val builder =
            IgdbQueryBuilder()
                .fields(*commonFields)
                .where(onlyGamesWhereClause)
                .sort("total_rating_count desc")
        limit?.let { builder.limit(it) }
        offset?.let { builder.offset(it) }

        return builder.build()
    }

    fun getGameById(id: Long): String =
        IgdbQueryBuilder()
            .fields(*commonFields)
            .where(WhereClause.condition("id = $id"))
            .limit(1)
            .build()

    fun getGamesById(ids: List<Long>): String {
        val builder =
            IgdbQueryBuilder()
                .fields(*commonFields)
                .where(WhereClause.condition("id = (${ids.joinToString(",")})"))
                .limit(100)
        return builder.build()
    }

    fun getMostPlayedGamesOnSteam(
        limit: Int?,
        offset: Int?,
    ): String {
        val builder =
            IgdbQueryBuilder()
                .fields(
                    "game_id",
                    "value",
                ).sort("value desc")
                .where(
                    WhereClause.condition("popularity_type = 5"),
                )

        limit?.let { builder.limit(it) }
        offset?.let { builder.offset(it) }

        return builder.build()
    }
}
