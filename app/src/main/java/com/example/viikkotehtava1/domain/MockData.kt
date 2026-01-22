package com.example.viikkotehtava1.domain

object MockData {
    val tasks = listOf(
        Task(
            id = 1,
            title = "Do the Kotlin assignment",
            description = "Code and test it",
            priority = 2,
            dueDate = "2026-01-20",
            done = false
        ),
        Task(
            id = 2,
            title = "Read Android Tutorial",
            description = "Read and try to learn",
            priority = 1,
            dueDate = "2026-01-18",
            done = true
        ),
        Task(
            id = 3,
            title = "Take the dog out",
            description = "Go for a walk w the dog",
            priority = 3,
            dueDate = "2026-01-22",
            done = false
        ),
        Task(
            id = 4,
            title = "Test app",
            description = "Run tests",
            priority = 2,
            dueDate = "2026-01-25",
            done = false
        ),
        Task(
            id = 5,
            title = "Document the code",
            description = "Write readme and make the tutorial video + apk",
            priority = 1,
            dueDate = "2026-01-30",
            done = false
        )
    )
}