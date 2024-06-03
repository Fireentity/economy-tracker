package it.unipd.dei.music_application.ui


import android.app.TimePickerDialog
import android.content.Context
import android.widget.TimePicker
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import it.unipd.dei.music_application.database.BalanceDatabase
import it.unipd.dei.music_application.view.MovementWithCategoryViewModel
import it.unipd.dei.music_application.daos.MovementDao
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import it.unipd.dei.music_application.daos.CategoryDao
import it.unipd.dei.music_application.models.Category
import it.unipd.dei.music_application.ui.components.MovementCard
import it.unipd.dei.music_application.ui.components.MovementDialog
import it.unipd.dei.music_application.utils.Constants.ALL_CATEGORIES_IDENTIFIER
import it.unipd.dei.music_application.view.TestViewModel
import java.time.LocalDateTime
import java.util.UUID

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
                )
                TextField(
                    value = selectedTime,
                    onValueChange = { },
                    label = { Text(text = "seleziona data") },
                    modifier = Modifier.clickable { timePickerDialog.show() },
                    readOnly = true
                )
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
    modifier: Modifier = Modifier,
) {
    val movementWithCategoryViewModel: MovementWithCategoryViewModel = hiltViewModel();
    val movementDao: MovementDao = db.getMovementDao()
    val categoryDao: CategoryDao = db.getCategoryDao()
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

    Surface(
        modifier = modifier
    ) {
        val context = LocalContext.current
        val categoryList = testViewModel.categoryNames.toMutableList()
        categoryList.add(0, ALL_CATEGORIES_IDENTIFIER)

        var showDialog by rememberSaveable { mutableStateOf(false) }
        var expandedDropdownMenu by remember { mutableStateOf(false) }
        var selectedCategory by rememberSaveable { mutableStateOf( categoryList[0] ) }

        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Row(
                            modifier = Modifier.fillMaxHeight(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "Registro",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f).fillMaxWidth()
                            )

                            //Spacer(modifier = Modifier.weight(1f).fillMaxWidth())

                            Box(
                                modifier = Modifier.fillMaxWidth().weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                /*IconButton(onClick = { expandedDropdownMenu = true }) {
                                    Icon(Icons.Filled.ArrowDropDown, contentDescription = "Menu")
                                }*/
                                ExposedDropdownMenuBox(
                                    expanded = expandedDropdownMenu,
                                    onExpandedChange = { expandedDropdownMenu = it }
                                ) {
                                    TextField(
                                        value = selectedCategory,
                                        onValueChange = {},
                                        readOnly = true,
                                        trailingIcon = {
                                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdownMenu)
                                        },
                                        modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true),
                                        colors = ExposedDropdownMenuDefaults.textFieldColors()
                                    )
                                    
                                    ExposedDropdownMenu(
                                        expanded = expandedDropdownMenu,
                                        onDismissRequest = { expandedDropdownMenu = false }
                                    ) {
                                        for (category in categoryList) {
                                            DropdownMenuItem(
                                                text = { Text(text = category) },
                                                onClick = {
                                                    selectedCategory = category
                                                }
                                            )
                                        }

                                    }
                                }
                                /*DropdownMenu(
                                    expanded = expandedDropdownMenu,
                                    onDismissRequest = { expandedDropdownMenu = false },
                                    content = {
                                        for (category in categoryList) {
                                            Text(text = category)
                                        }
                                    }
                                )*/
                            }

                        }



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
        ) { paddingValues ->
            Column(
                modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                var selectedTabIndex by rememberSaveable {
                    mutableIntStateOf(1)
                }

                TabRow(
                    selectedTabIndex = selectedTabIndex
                ) {

                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = {
                            selectedTabIndex = 0

                        },
                        text = { Text(text = "Entrate") },
                    )
                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = {
                            selectedTabIndex = 1

                        },
                        text = { Text(text = "Tutti") },
                    )
                    Tab(
                        selected = selectedTabIndex == 2,
                        onClick = {
                            selectedTabIndex = 2

                        },
                        text = { Text(text = "Uscite") },
                    )
                }

                if (showDialog) {
                    MovementDialog(
                        onDismiss = { showDialog = false },
                        onConfirm = { inputText ->
                            showDialog = false
                        }
                    )
                }

                val category = Category(
                    UUID.randomUUID(),
                    selectedCategory,
                    System.currentTimeMillis(),
                    System.currentTimeMillis()
                )

                testViewModel.createDummyDataIfNoMovement()
                movementWithCategoryViewModel.loadInitialMovements()

                val movements = movementWithCategoryViewModel.getMovements()
                    .observeAsState(initial = emptyList())
                val negativeMovements = movementWithCategoryViewModel.getNegativeMovement()
                    .observeAsState(initial = emptyList())
                val positiveMovements = movementWithCategoryViewModel.getPositiveMovement()
                    .observeAsState(initial = emptyList())


                movementWithCategoryViewModel.loadTotalAmountsByCategory(category)

                DisplayBalance(positiveMovements = positiveMovements, negativeMovements = negativeMovements)

                when(selectedTabIndex) {
                    0 -> LazyColumn(
                        modifier = modifier
                    ) {
                        items(positiveMovements.value.size) { index ->
                            MovementCard(positiveMovements.value[index])
                        }
                    }
                    1 -> LazyColumn(
                        modifier = modifier
                    ) {
                        items(movements.value.size) { index ->
                            MovementCard(movements.value[index])
                        }
                    }
                    2 -> LazyColumn(
                        modifier = modifier
                    ) {
                        items(negativeMovements.value.size) { index ->
                            MovementCard(negativeMovements.value[index])
                        }
                    }
                }
            }
        }
    }
}