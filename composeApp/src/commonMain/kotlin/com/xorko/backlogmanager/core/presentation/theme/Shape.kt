package com.xorko.backlogmanager.core.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes

val BacklogManagerShapes =
    Shapes(
        small = RoundedCornerShape(Dimens.roundingSmall),
        medium = RoundedCornerShape(Dimens.roundingMedium),
        large = RoundedCornerShape(Dimens.roundingLarge),
    )
