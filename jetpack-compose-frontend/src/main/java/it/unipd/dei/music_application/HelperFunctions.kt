package it.unipd.dei.music_application

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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