package com.xorko.backlogmanager.game.presentation.backloggedgamelist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerTheme
import com.xorko.backlogmanager.game.domain.models.BackloggedGame
import com.xorko.backlogmanager.game.domain.models.BackloggedGame.Dropped
import com.xorko.backlogmanager.game.domain.models.BackloggedGame.Finished
import com.xorko.backlogmanager.game.domain.models.BackloggedGame.Playing
import com.xorko.backlogmanager.game.domain.models.BackloggedGame.ToPlay
import com.xorko.backlogmanager.game.presentation.backloggedgamelist.getStatus
import kotlin.reflect.KClass

private val statuses =
    listOf(
        null,
        ToPlay::class,
        Playing::class,
        Finished::class,
        Dropped::class,
    )

@Composable
fun BackloggedGameStatusFilter(
    selectedStatus: KClass<out BackloggedGame>?,
    onStatusSelected: (KClass<out BackloggedGame>?) -> Unit,
    counts: Map<KClass<out BackloggedGame>, Int>,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(statuses) { status ->
            val count = if (status == null) counts.values.sum() else counts[status] ?: 0

            FilterChip(
                selected = selectedStatus == status,
                onClick = { onStatusSelected(status) },
                label = {
                    Column(
                        modifier = Modifier.padding(vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(text = status.getStatus().asString())
                        Text(
                            text = "($count)",
                            style = MaterialTheme.typography.labelSmall,
                            color = LocalContentColor.current.copy(alpha = 0.7f),
                        )
                    }
                },
                leadingIcon = {
                    if (selectedStatus == status) {
                        Icon(
                            Icons.Outlined.Check,
                            contentDescription = null,
                            modifier = Modifier.size(FilterChipDefaults.IconSize),
                        )
                    }
                },
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    BacklogManagerTheme {
        BackloggedGameStatusFilter(
            selectedStatus = Playing::class,
            onStatusSelected = {},
            counts =
                mapOf(
                    ToPlay::class to 1,
                    Playing::class to 2,
                    Finished::class to 3,
                ),
        )
    }
}
