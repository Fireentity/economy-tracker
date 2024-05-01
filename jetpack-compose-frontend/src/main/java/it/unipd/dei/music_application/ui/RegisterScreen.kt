package it.unipd.dei.music_application.ui


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import it.unipd.dei.music_application.MyLazyColumn

data class TabItem(
    val title: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(modifier: Modifier = Modifier) {
    //val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val tabItems = listOf(
        TabItem(title = "Tutti"),
        TabItem(title = "Uscite"),
        TabItem(title = "Entate"),
    )

    Surface(
        modifier = modifier
    ) {
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
                    onClick = { /*TODO*/ }
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

                MyLazyColumn(modifier = Modifier.fillMaxSize())
            }
        }
    }

}