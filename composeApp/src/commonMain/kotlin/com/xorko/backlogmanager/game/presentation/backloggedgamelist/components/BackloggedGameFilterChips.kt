package com.xorko.backlogmanager.game.presentation.backloggedgamelist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xorko.backlogmanager.core.presentation.theme.Dimens
import com.xorko.backlogmanager.game.domain.BackloggedGame
import com.xorko.backlogmanager.game.domain.BackloggedGame.Finished
import com.xorko.backlogmanager.game.domain.BackloggedGame.NowPlaying
import com.xorko.backlogmanager.game.domain.BackloggedGame.ToPlay
import com.xorko.backlogmanager.game.presentation.backloggedgamelist.getStatus
import kotlin.reflect.KClass

private val statuses =
    listOf<KClass<out BackloggedGame>?>(
        null,
        ToPlay::class,
        NowPlaying::class,
        Finished::class,
    )

@Composable
fun BackloggedGameFilterChips(
    selectedStatus: KClass<out BackloggedGame>?,
    onStatusSelected: (KClass<out BackloggedGame>?) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(Dimens.spacingMedium),
        modifier = modifier,
    ) {
        items(statuses) { status ->
            FilterChip(
                selected = selectedStatus == status,
                onClick = { onStatusSelected(status) },
                label = {
                    Text(
                        text = status.getStatus().asString(),
                        style = MaterialTheme.typography.labelMedium,
                    )
                },
                leadingIcon = {
                    if (selectedStatus == status) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                        )
                    }
                },
                shape = RoundedCornerShape(50.dp),
                colors =
                    FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
            )
        }
    }
}
