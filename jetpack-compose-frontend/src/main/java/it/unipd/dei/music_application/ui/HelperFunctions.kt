package it.unipd.dei.music_application.ui

import android.content.Context
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import it.unipd.dei.music_application.models.Category
import it.unipd.dei.music_application.utils.DisplayToast.displayFailure
import it.unipd.dei.music_application.utils.DisplayToast.displaySuccess
import it.unipd.dei.music_application.view.MovementWithCategoryViewModel
import java.util.UUID

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
fun DisplayBalance(movementWithCategoryViewModel: MovementWithCategoryViewModel) {
    Column(
        modifier = Modifier
            .padding(top = 3.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Bilancio:")

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "€%.2f".format(movementWithCategoryViewModel.totalPositiveAmount.observeAsState(initial = 0.0).value),
                color = Color(0xFF15803D),
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                textAlign = TextAlign.Center
            )
            //Spacer(modifier = Modifier.weight(1.1f))
            Text(
                text = "€%.2f".format(movementWithCategoryViewModel.totalAllAmount.observeAsState(initial = 0.0).value),
                color = if (movementWithCategoryViewModel.totalAllAmount.observeAsState(initial = 0.0).value >=0) Color(0xFF15803D) else Color(0xFFB91C1C),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                textAlign = TextAlign.Center
            )
            //Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "€%.2f".format(movementWithCategoryViewModel.totalNegativeAmount.observeAsState(initial = 0.0).value),
                color = Color(0xFFB91C1C),
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
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

object CategorySaver {
    val saver = mapSaver(
        save = { category: Category ->
            mapOf(
                "uuid" to category.uuid,
                "identifier" to category.identifier,
                "createdAt" to category.createdAt,
                "updatedAt" to category.updatedAt
            )
        },
        restore = { map ->
            Category(
                map["uuid"] as UUID,
                map["identifier"] as String,
                map["createdAt"] as Long,
                map["updatedAt"] as Long
            )
        }
    )
}

@Composable
fun CheckUpsertResult(movementWithCategoryViewModel: MovementWithCategoryViewModel, context: Context) {

    val upsertResult = movementWithCategoryViewModel.upsertResult.observeAsState().value

    if(upsertResult == true) {
        displaySuccess(context)
    }
    else{
        displayFailure(context)
    }
}