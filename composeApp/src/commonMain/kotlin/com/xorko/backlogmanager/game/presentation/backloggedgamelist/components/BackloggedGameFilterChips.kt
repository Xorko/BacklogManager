package com.xorko.backlogmanager.game.presentation.backloggedgamelist.components

import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
fun BackloggedGameFilterChips(
    selectedStatus: KClass<out BackloggedGame>?,
    onStatusSelected: (KClass<out BackloggedGame>?) -> Unit,
    modifier: Modifier = Modifier,
) {
    SingleChoiceSegmentedButtonRow(
        modifier =
        modifier,
    ) {
        statuses.forEachIndexed { index, status ->
            SegmentedButton(
                selected = selectedStatus == status,
                onClick = { onStatusSelected(status) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = statuses.size),
            ) {
                Text(text = status.getStatus().asString())
            }
        }
    }
}
