package com.example.viikkotehtava1.domain

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class TaskViewModel : ViewModel() {
    var tasks by mutableStateOf(MockData.tasks)

    fun addTask(task: Task) {
        tasks = tasks + task
    }

    fun toggleDone(id: Int) {
        tasks = tasks.map { if (it.id == id) it.copy(done = !it.done) else it }
    }

    fun removeTask(id: Int) {
        tasks = tasks.filter { it.id != id }
    }

    fun filterByDone(done: Boolean) {
        tasks = tasks.filter { it.done == done }
    }

    fun sortByDueDate() {
        tasks = tasks.sortedBy { it.dueDate }
    }
}