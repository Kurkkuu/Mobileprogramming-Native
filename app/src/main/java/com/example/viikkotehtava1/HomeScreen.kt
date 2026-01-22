package com.example.viikkotehtava1

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.viikkotehtava1.domain.Task
import com.example.viikkotehtava1.domain.TaskViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel = viewModel()
) {
    var newTaskTitle by remember { mutableStateOf("") }
    var showOnlyDone by remember { mutableStateOf(false) }
    var sortByDate by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "My Tasks",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = newTaskTitle,
                onValueChange = { newTaskTitle = it },
                label = { Text("New Task") },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    if (newTaskTitle.isNotBlank()) {
                        val newTask = Task(
                            id = viewModel.tasks.maxOfOrNull { it.id }?.plus(1) ?: 1,
                            title = newTaskTitle,
                            description = "",
                            priority = 1,
                            dueDate = "2026-01-30",
                            done = false
                        )
                        viewModel.addTask(newTask)
                        newTaskTitle = ""
                    }
                }
            ) {
                Text("Add")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { showOnlyDone = !showOnlyDone }
            ) {
                Text(if (showOnlyDone) "Show all" else "Show only completed")
            }

            Button(
                onClick = { sortByDate = !sortByDate }
            ) {
                Text(if (sortByDate) "Default filter" else "Filter by date")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val filteredTasks = if (showOnlyDone) {
            viewModel.tasks.filter { it.done }
        } else {
            viewModel.tasks
        }

        val sortedTasks = if (sortByDate) {
            filteredTasks.sortedBy { it.dueDate }
        } else {
            filteredTasks
        }

        LazyColumn {
            items(
                items = sortedTasks,
                key = { it.id }
            ) { task ->
                TaskItem(
                    task = task,
                    onToggle = { viewModel.toggleDone(task.id) },
                    onDelete = { viewModel.removeTask(task.id) }
                )
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Checkbox(
                checked = task.done,
                onCheckedChange = { onToggle() }
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = task.title,
                    fontSize = 18.sp
                )
                Text(
                    text = "Due: ${task.dueDate}",
                    fontSize = 14.sp
                )
            }

            Button(
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Remove")
            }
        }
    }
}