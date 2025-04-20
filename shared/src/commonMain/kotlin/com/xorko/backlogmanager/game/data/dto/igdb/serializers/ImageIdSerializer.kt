package com.xorko.backlogmanager.game.data.dto.igdb.serializers

import com.xorko.backlogmanager.game.data.dto.igdb.IgdbImageId
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object ImageIdSerializer : KSerializer<IgdbImageId> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("ImageId", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: IgdbImageId,
    ) {
        encoder.encodeString(value.value)
    }

    override fun deserialize(decoder: Decoder): IgdbImageId {
        val value = decoder.decodeString()
        return IgdbImageId(value)
    }
}
