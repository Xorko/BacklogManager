package com.xorko.backlogmanager.game.data.dto.igdb

import com.xorko.backlogmanager.game.data.dto.igdb.serializers.ImageIdSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IgdbCoverDto(
    val id: Long,
    @SerialName("image_id") @Serializable(with = ImageIdSerializer::class) val imageId: IgdbImageId?,
)