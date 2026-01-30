package com.example.viikkotehtava1.domain

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object TaskRepository {
    private val _tasks = MutableStateFlow(MockData.tasks)
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    fun addTask(task: Task) {
        _tasks.value += task
    }

    fun toggleDone(id: Int) {
        _tasks.value = _tasks.value.map {
            if (it.id == id) it.copy(done = !it.done) else it
        }
    }

    fun removeTask(id: Int) {
        _tasks.value = _tasks.value.filter { it.id != id }
    }

    fun updateTask(updated: Task) {
        _tasks.value = _tasks.value.map {
            if (it.id == updated.id) updated else it
        }
    }
}