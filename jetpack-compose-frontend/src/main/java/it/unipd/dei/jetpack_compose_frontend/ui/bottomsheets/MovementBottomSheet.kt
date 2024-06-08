package it.unipd.dei.jetpack_compose_frontend.ui.bottomsheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.unipd.dei.common_backend.models.MovementWithCategory
import it.unipd.dei.jetpack_compose_frontend.ui.cards.MovementCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovementBottomSheet(movementWithCategory: MovementWithCategory) {
    ModalBottomSheet(onDismissRequest = { }) {
        Column(
            modifier = Modifier.padding(5.dp)
        ) {

            MovementCard(movementWithCategory)

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { /* Handle edit button click */ },
                    modifier = Modifier.padding(top = 30.dp, bottom = 15.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Edit"
                    )
                    Text(text = "Modifica")
                }

                Button(
                    onClick = { /* Handle delete button click */ },
                    modifier = Modifier.padding(top = 15.dp, bottom = 15.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete"
                    )
                    Text(text = "Elimina")
                }
            }
        }
    }
}