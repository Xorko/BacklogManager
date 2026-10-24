package com.xorko.backlogmanager.game.presentation.backloggedgamelist.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerTheme
import com.xorko.backlogmanager.game.domain.models.BackloggedGame
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
    )

@Composable
fun BackloggedGameStatusFilter(
    selectedStatus: KClass<out BackloggedGame>?,
    onStatusSelected: (KClass<out BackloggedGame>?) -> Unit,
    counts: Map<KClass<out BackloggedGame>, Int>,
    modifier: Modifier = Modifier,
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier,
    ) {
        statuses.forEachIndexed { index, status ->
            SegmentedButton(
                selected = selectedStatus == status,
                onClick = { onStatusSelected(status) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = statuses.size),
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val count = when (status) {
                        null -> counts.values.sum()
                        else -> counts[status] ?: 0
                    }

                    Text(text = status.getStatus().asString())
                    Text(
                        text = "($count)",
                        style = MaterialTheme.typography.labelSmall,
                        color = LocalContentColor.current.copy(alpha = 0.7f),
                    )
                }
            }
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
            counts = mapOf(
                ToPlay::class to 1,
                Playing::class to 2,
                Finished::class to 3,
            ),
        )
    }
}