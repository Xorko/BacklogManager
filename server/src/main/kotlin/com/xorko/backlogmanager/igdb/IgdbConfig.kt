package com.xorko.backlogmanager.igdb

object IgdbConfig {
    val clientId =
        System.getenv("IGDB_CLIENT_ID")
            ?: error("Missing IGDB client ID")

    val clientSecret =
        System.getenv("IGDB_CLIENT_SECRET")
            ?: error("Missing IGDB client secret")

    const val BASE_URL = "https://api.igdb.com/v4"
    const val TOKEN_URL = "https://id.twitch.tv/oauth2/token"

    const val CLIENT_ID_HEADER = "Client-ID"
}
