package it.unipd.dei.music_application.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import it.unipd.dei.music_application.models.MovementWithCategory

@Composable
fun MyDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 10.dp)
    )
}

@Composable
fun MyLazyColumn(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
    ) {
        val textModifier = Modifier.padding(16.dp)

        items(20) { index ->
            Text(text = "List items : $index", modifier = textModifier)
            MyDivider()
        }
    }
}

@Composable
fun DisplayBalance(positiveMovements: State<List<MovementWithCategory>>, negativeMovements: State<List<MovementWithCategory>>) {
    Column(
        modifier = Modifier
            .padding(top = 3.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var positiveAmount = 0.0
        for (movement in positiveMovements.value) {
            positiveAmount += movement.movement.amount
        }

        var negativeAmount = 0.0
        for (movement in negativeMovements.value) {
            negativeAmount += movement.movement.amount
        }

        Text(text = "Bilancio:")

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "€%.2f".format(positiveAmount),
                color = Color(0xFF15803D),
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth().weight(1f),
                textAlign = TextAlign.Center
            )
            //Spacer(modifier = Modifier.weight(1.1f))
            Text(
                text = "€%.2f".format(positiveAmount + negativeAmount),
                color = if (positiveAmount + negativeAmount >=0) Color(0xFF15803D) else Color(0xFFB91C1C),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth().weight(1f),
                textAlign = TextAlign.Center
            )
            //Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "€%.2f".format(negativeAmount),
                color = Color(0xFFB91C1C),
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth().weight(1f),
                textAlign = TextAlign.Center
            )
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 4.dp,
            color = Color.White
        )
    }
}