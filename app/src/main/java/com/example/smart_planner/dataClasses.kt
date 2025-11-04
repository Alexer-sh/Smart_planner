package com.example.smart_planner

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import java.time.LocalDateTime


data class Task(
    val title: String,
    val description: String? = null,
    val priority: Int = 1,
    val isImportantFlag: Boolean = false,
    val expDate: LocalDateTime? = null,
    var isDone: MutableState<Boolean> = mutableStateOf(false)
)