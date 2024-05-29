package it.unipd.dei.music_application.ui


import android.app.TimePickerDialog
import android.content.Context
import android.widget.TimePicker
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.RoomDatabase
import it.unipd.dei.music_application.MyLazyColumn
import it.unipd.dei.music_application.database.BalanceDatabase
import it.unipd.dei.music_application.view.CategoryTotalViewModel
import it.unipd.dei.music_application.view.MovementWithCategoryViewModel
import it.unipd.dei.music_application.view.TestViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import it.unipd.dei.music_application.MyDivider
import it.unipd.dei.music_application.daos.BalanceDao
import it.unipd.dei.music_application.daos.CategoryDao
import it.unipd.dei.music_application.daos.MovementDao
import it.unipd.dei.music_application.models.MovementWithCategory
import it.unipd.dei.music_application.models.CategoryTotal
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import java.time.LocalDateTime

data class TabItem(
    val title: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTimePicker() {
    var dateTime = LocalDateTime.now()

    val timePickerState = remember {
        TimePickerState(
            is24Hour = true,
            initialHour = dateTime.hour,
            initialMinute = dateTime.minute
        )
    }

    TimePicker(state = timePickerState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInputDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var amountFieldState by remember { mutableStateOf(TextFieldValue()) }
    var date by remember { mutableStateOf(TextFieldValue()) }

    var selectedTime by remember { mutableStateOf("") }

    val context = LocalContext.current
    //val calendar = Calendar.getInstance()
    var dateTime = LocalDateTime.now()
    val hour = dateTime.hour
    val minute = dateTime.minute

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            selectedTime = String.format("%02d:%02d", hourOfDay, minute)
        },
        hour, minute, true
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Text Inverter") },
        text = {
            Column {
                TextField(
                    value = amountFieldState,
                    onValueChange = { amountFieldState = it },
                    label = { Text("Ammontare") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                );
                TextField(
                    value = selectedTime,
                    onValueChange = { },
                    label = { Text(text = "seleziona data") },
                    modifier = Modifier.clickable { timePickerDialog.show() },
                    readOnly = true
                );
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(amountFieldState.text) }) {
                Text("Submit")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Dismiss")
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    db: BalanceDatabase,
    modifier: Modifier = Modifier
) {
    //val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val balanceDao: BalanceDao = db.getBalanceDao()
    val movementDao: MovementDao = db.getMovementDao()
    val categoryDao: CategoryDao = db.getCategoryDao()

    val categoryTotalViewModel = viewModel<CategoryTotalViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CategoryTotalViewModel(
                    categoryDao
                ) as T
            }
        }
    )

    val movementWithCategoryViewModel = viewModel<MovementWithCategoryViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MovementWithCategoryViewModel(
                    movementDao
                ) as T
            }
        }
    )

    val testViewModel = viewModel<TestViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TestViewModel(
                    categoryDao,
                    movementDao
                ) as T
            }
        }
    )

    //testViewModel.createDummyDataIfNoMovement()


    val tabItems = listOf(
        TabItem(title = "Tutti"),
        TabItem(title = "Uscite"),
        TabItem(title = "Entate"),
    )

    Surface(
        modifier = modifier
    ) {
        val context = LocalContext.current

        var showDialog by rememberSaveable { mutableStateOf(false) }
        //var text by rememberSaveable { mutableStateOf("") }
        var reversedText by rememberSaveable { mutableStateOf("") }
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(
                            "Page title",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    //scrollBehavior = scrollBehavior,
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        showDialog = true
                    }
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "")
                }
            }
        ) {
            paddingValues ->
            Column(
                modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                var selectedTabIndex by rememberSaveable {
                    mutableIntStateOf(0)
                }

                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier = Modifier
                                .tabIndicatorOffset(tabPositions[selectedTabIndex])
                                //.padding(horizontal = 25.dp)
                                .wrapContentWidth(),
                            height = 2.dp,
                        )
                    }
                    //divider = { }
                    ) {
                    tabItems.forEachIndexed { index, item ->
                        Tab(
                            selected = index == selectedTabIndex,
                            onClick = { selectedTabIndex = index },
                            text = { Text(text = item.title) },
                        )
                    }
                }

                if (showDialog) {
                    TextInputDialog(
                        onDismiss = { showDialog = false },
                        onConfirm = { inputText ->
                            showDialog = false
                            reversedText = inputText.reversed()
                        }
                    )
                }
                
                //Text(text = reversedText)

                //MyLazyColumn(modifier = Modifier.fillMaxSize())

                testViewModel.createDummyDataIfNoMovement()
                movementWithCategoryViewModel.loadInitialMovements()
                val movements = movementWithCategoryViewModel.allData.observeAsState(initial = emptyList())
                //Text(text = "${movements.value.size}")
                LazyColumn(
                    modifier = modifier
                ) {
                    val textModifier = Modifier.padding(16.dp)

                    items(movements.value.size) { index ->
                        Text(text = "${movements.value.get(index = index).movement.amount}", modifier = textModifier)
                        MyDivider()
                    }
                }
            }
        }
    }

}