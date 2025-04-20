package com.xorko.backlogmanager.game.data.dto.igdb

import kotlinx.serialization.Serializable

@Serializable
data class IgdbImageId(
    val value: String,
) {
    fun toCoverUrl(size: IgdbImageSize = IgdbImageSize.COVER_BIG): String = "https://images.igdb.com/igdb/image/upload/t_$size/$value.jpg"
}

enum class IgdbImageSize(
    val raw: String,
) {
    THUMB("thumb"),
    COVER_SMALL("cover_small"),
    COVER_BIG("cover_big"),
    LOGO_MED("logo_med"),
    SCREENSHOT_BIG("screenshot_big"),
    SCREENSHOT_HUGE("screenshot_huge"),
    MEDIUM("720p"),
    HD("1080p"),
    ;

    override fun toString(): String = raw
}
