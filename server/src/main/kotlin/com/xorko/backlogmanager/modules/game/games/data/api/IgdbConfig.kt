package com.xorko.backlogmanager.modules.game.games.data.api

object IgdbConfig {
    val clientId =
        System.getenv("IGDB_CLIENT_ID")
            ?: error("Missing IGDB client ID")

    val clientSecret =
        System.getenv("IGDB_CLIENT_SECRET")
            ?: error("Missing IGDB client secret")
}
