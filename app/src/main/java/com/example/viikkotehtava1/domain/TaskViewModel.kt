package com.example.viikkotehtava1.domain

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

class TaskViewModel : ViewModel() {
    private val repository = TaskRepository

    val tasks: StateFlow<List<Task>> = repository.tasks

    fun addTask(task: Task) = repository.addTask(task)
    fun toggleDone(id: Int) = repository.toggleDone(id)
    fun removeTask(id: Int) = repository.removeTask(id)
    fun updateTask(task: Task) = repository.updateTask(task)
}