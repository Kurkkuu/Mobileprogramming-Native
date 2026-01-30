package com.example.viikkotehtava1.domain

object MockData {
    val tasks = listOf(
        Task(1, "Do the Kotlin assignment", "Code and test it", 2, "2026-01-20", false),
        Task(2, "Read Android Tutorial", "Read and try to learn", 1, "2026-01-18", true),
        Task(3, "Take the dog out", "Go for a walk w the dog", 3, "2026-01-22", false),
        Task(4, "Test app", "Run tests", 2, "2026-01-25", false),
        Task(
            5,
            "Document the code",
            "Write readme and make the tutorial video + apk",
            1,
            "2026-01-30",
            false
        )
    )
}