package com.xorko.backlogmanager.game.data.network

object IgdbQueryFactory {
    fun searchGames(
        query: String,
        limit: Int? = null,
        offset: Int? = null,
    ): String {
        val builder =
            IgdbQueryBuilder()
                .search(query)
                .fields(
                    "id",
                    "name",
                    "summary",
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
                ).where(
                    WhereClause.and(
                        WhereClause.condition("version_parent = null"),
                        WhereClause.or(
                            WhereClause.condition("category = 0"), // Main Game
                            WhereClause.condition("category = 8"), // Expanded Game
                        ),
                    ),
                )
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
                .fields(
                    "id",
                    "name",
                    "summary",
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
                ).where(
                    WhereClause.and(
                        WhereClause.condition("version_parent = null"),
                        WhereClause.or(
                            WhereClause.condition("category = 0"), // Main Game
                            WhereClause.condition("category = 8"), // Expanded Game
                        ),
                    ),
                )
        limit?.let { builder.limit(it) }
        offset?.let { builder.offset(it) }

        return builder.build()
    }

    fun getGameById(id: Long): String =
        IgdbQueryBuilder()
            .fields(
                "id",
                "name",
                "summary",
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
            ).where(WhereClause.condition("id = $id"))
            .limit(1)
            .build()
}
