package com.example.viikkotehtava1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.viikkotehtava1.domain.Task
import com.example.viikkotehtava1.domain.viewmodel.TaskViewModel
import com.example.viikkotehtava1.ui.theme.Viikkotehtava1Theme
import com.example.viikkotehtava1.view.DetailScreen
import com.example.viikkotehtava1.view.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { App() }
    }
}

@Composable
fun App() {
    val viewModel: TaskViewModel = viewModel()
    var selectedTask by remember { mutableStateOf<Task?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    Viikkotehtava1Theme {
        HomeScreen(
            modifier = Modifier.fillMaxSize(),
            viewModel = viewModel,
            onTaskClick = { selectedTask = it },
            onAddClick = { showAddDialog = true }
        )

        selectedTask?.let { task ->
            DetailScreen(
                task = task,
                onDismiss = { selectedTask = null },
                onSave = {
                    viewModel.updateTask(it)
                    selectedTask = null
                },
                onDelete = {
                    viewModel.removeTask(it.id)
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