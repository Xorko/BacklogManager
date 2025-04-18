package com.xorko.backlogmanager.game.data.dto.serializers

import com.xorko.backlogmanager.game.data.dto.ImageId
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object ImageIdSerializer : KSerializer<ImageId> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ImageId", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: ImageId,
    ) {
        encoder.encodeString(value.value)
    }

    override fun deserialize(decoder: Decoder): ImageId {
        val value = decoder.decodeString()
        return ImageId(value)
    }
}
