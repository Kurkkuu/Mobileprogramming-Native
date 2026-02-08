package com.example.viikkotehtava1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.viikkotehtava1.domain.Task
import com.example.viikkotehtava1.domain.viewmodel.TaskViewModel
import com.example.viikkotehtava1.ui.theme.Viikkotehtava1Theme
import com.example.viikkotehtava1.view.DetailScreen
import com.example.viikkotehtava1.AddTaskDialog

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val navController = rememberNavController()
    val viewModel: TaskViewModel = viewModel()

    var selectedTask by remember { mutableStateOf<Task?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    Viikkotehtava1Theme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    val items = listOf(
                        Triple(Routes.HOME, "Tasks", Icons.Default.Home),
                        Triple(Routes.CALENDAR, "Calendar", Icons.Default.DateRange),
                        Triple(Routes.SETTINGS, "Settings", Icons.Default.Settings)
                    )

                    items.forEach { (route, label, icon) ->
                        NavigationBarItem(
                            icon = { Icon(icon, contentDescription = label) },
                            label = { Text(label) },
                            selected = currentDestination?.hierarchy?.any { it.route == route } == true,
                            onClick = {
                                navController.navigate(route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = Routes.HOME,
                modifier = Modifier.padding(padding)
            ) {
                composable(Routes.HOME) {
                    HomeScreen(
                        viewModel = viewModel,
                        onTaskClick = { task -> selectedTask = task },
                        onAddClick = { showAddDialog = true }
                    )
                }

                composable(Routes.CALENDAR) {
                    CalendarScreen(
                        viewModel = viewModel,
                        onTaskClick = { task -> selectedTask = task }
                    )
                }

                composable(Routes.SETTINGS) {
                    SettingsScreen()
                }
            }
        }

        if (selectedTask != null) {
            DetailScreen(
                task = selectedTask!!,
                onDismiss = { selectedTask = null },
                onSave = { updatedTask ->
                    viewModel.updateTask(updatedTask)
                    selectedTask = null
                },
                onDelete = { taskToDelete ->
                    viewModel.removeTask(taskToDelete.id)
                    selectedTask = null
                }
            )
        }

        if (showAddDialog) {
            AddTaskDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { title, desc, priority, date ->
                    val newId = (viewModel.tasks.value.maxOfOrNull { it.id } ?: 0) + 1
                    viewModel.addTask(
                        Task(newId, title, desc, priority, date, false)
                    )
                    showAddDialog = false
                }
            )
        }
    }
}