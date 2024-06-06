package it.unipd.dei.music_application.ui


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.style.TextOverflow
import it.unipd.dei.music_application.database.BalanceDatabase
import it.unipd.dei.music_application.view.MovementWithCategoryViewModel
import it.unipd.dei.music_application.daos.MovementDao
import it.unipd.dei.music_application.daos.CategoryDao
import it.unipd.dei.music_application.models.Category
import it.unipd.dei.music_application.models.Movement
import it.unipd.dei.music_application.ui.components.MovementCard
import it.unipd.dei.music_application.ui.components.MovementDialog
import it.unipd.dei.music_application.utils.DisplayToast.displayFailure
import it.unipd.dei.music_application.utils.DisplayToast.displaySuccess
import it.unipd.dei.music_application.view.CategoryViewModel
import it.unipd.dei.music_application.view.TestViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

data class TabItem(
    val title: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    db: BalanceDatabase,
    movementWithCategoryViewModel: MovementWithCategoryViewModel,
    categoryViewModel: CategoryViewModel,
    testViewModel: TestViewModel,
    modifier: Modifier = Modifier,
) {
    val movementDao: MovementDao = db.getMovementDao()
    val categoryDao: CategoryDao = db.getCategoryDao()
    /*val testViewModel = viewModel<TestViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TestViewModel(
                    categoryDao,
                    movementDao
                ) as T
            }
        }
    )*/

    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        val context = LocalContext.current

        val categoryList = categoryViewModel.allCategories.observeAsState(initial = listOf()).value

        var showDialog by rememberSaveable { mutableStateOf(false) }
        var expandedDropdownMenu by remember { mutableStateOf(false) }
        //var selectedCategory by rememberSaveable { mutableStateOf(categoryIdentifiers[0]) }
        var selectedCategory = rememberSaveable(saver = CategorySaver.saver) { Category(
            categoryList[0].uuid,
            categoryList[0].identifier,
            categoryList[0].createdAt,
            categoryList[0].updatedAt
        ) }

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
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                            )

                            //Spacer(modifier = Modifier.weight(1f).fillMaxWidth())

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                contentAlignment = Alignment.Center
                            ) {

                                ExposedDropdownMenuBox(
                                    expanded = expandedDropdownMenu,
                                    onExpandedChange = { expandedDropdownMenu = it }
                                ) {
                                    TextField(
                                        value = selectedCategory.identifier,
                                        onValueChange = {},
                                        label = { Text(text = "Categoria") },
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
                                                text = { Text(text = category.toString()) },
                                                onClick = {
                                                    selectedCategory = category
                                                    expandedDropdownMenu = false
                                                    movementWithCategoryViewModel.loadTotalAmountsByCategory(selectedCategory)
                                                    movementWithCategoryViewModel.loadInitialMovementsByCategory(selectedCategory)
                                                }
                                            )
                                        }

                                    }
                                }

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
                        onConfirm = { stringAmount, category, date ->
                            showDialog = false

                            try {
                                val amount = stringAmount.toDouble()

                                if (category.identifier != "" && date != "" ) {
                                    val year = date.split("/")[2].toInt()
                                    val month = date.split("/")[1].toInt()
                                    val day = date.split("/")[0].toInt()

                                    val localDate = LocalDate.of(year, month, day)
                                    val startOfDay = localDate.atStartOfDay()
                                    val instant = startOfDay.atZone(ZoneId.systemDefault()).toInstant()
                                    val timestamp = instant.toEpochMilli()

                                    val movement: Movement = Movement(
                                        uuid = UUID.randomUUID(),
                                        amount = String.format("%.2f", amount).toDouble(),
                                        categoryId = category.uuid,
                                        createdAt = timestamp,
                                        updatedAt = timestamp
                                    )
                                    movementWithCategoryViewModel.upsertMovement(movement)
                                    displaySuccess(context)
                                }
                                else{
                                    displayFailure(context)
                                }
                            }
                            catch (e: NumberFormatException) {
                                displayFailure(context)
                            }

                        },
                        categoryViewModel
                    )
                }

                /*val category = Category(
                    UUID.randomUUID(),
                    selectedCategory,
                    System.currentTimeMillis(),
                    System.currentTimeMillis()
                )*/

                movementWithCategoryViewModel.loadInitialMovementsByCategory(selectedCategory)

                val movements = movementWithCategoryViewModel.allData
                    .observeAsState(initial = emptyList())
                val negativeMovements = movementWithCategoryViewModel.negativeData
                    .observeAsState(initial = emptyList())
                val positiveMovements = movementWithCategoryViewModel.positiveData
                    .observeAsState(initial = emptyList())


                movementWithCategoryViewModel.loadTotalAmountsByCategory(selectedCategory)

                DisplayBalance(movementWithCategoryViewModel = movementWithCategoryViewModel)

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