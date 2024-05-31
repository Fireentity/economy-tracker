package it.unipd.dei.music_application

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import it.unipd.dei.music_application.models.MovementWithCategory
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import it.unipd.dei.music_application.R

@Composable
fun MyDivider() {
    Divider(
        modifier = Modifier.padding(horizontal = 10.dp)
    )
}

@Composable
fun MyLazyColumn (modifier: Modifier = Modifier) {
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
fun DisplayMovements(movements: State<List<MovementWithCategory>>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
    ) {
        //val textModifier = Modifier.padding(16.dp)

        items(movements.value.size) { index ->
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                //val date: Date = movements.value.get(index).movement.updatedAt
                val instant = Instant.ofEpochMilli(movements.value.get(index).movement.updatedAt)
                val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

                /*Image(
                    painter = painterResource(it.unipd.dei.music_application.R.drawable.),
                    contentDescription = "Circle Up",
                    modifier = Modifier.size(100.dp),
                    contentScale = ContentScale.Crop
                )*/

                Column {
                    Text(text = dateTime.format(formatter))
                    Text(text = movements.value.get(index).category.toString())
                }

                Spacer(modifier = Modifier.weight(1f))
                
                Text(
                    text = "€${movements.value.get(index).movement.amount}"
                )
            }
            
            MyDivider()
        }
    }
}