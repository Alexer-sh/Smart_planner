package com.example.smart_planner

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Composable
fun AddEditTaskScreen(
    taskList: MutableList<Task>,
    onBack: () -> Unit,
    existingTask: Task? = null,
    taskIndex: Int? = null
) {
    val title = remember { mutableStateOf(existingTask?.title ?: "") }
    val description = remember { mutableStateOf(existingTask?.description ?: "") }
    val priority = remember { mutableIntStateOf(existingTask?.priority ?: 1) }
    val isImportantFlag = remember { mutableStateOf(existingTask?.isImportantFlag ?: false) }
    val expDate = remember { mutableStateOf(existingTask?.expDate?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) ?: "") }

    //Объявненме всего интерфейся
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        //Заголовок
        Text(
            if (existingTask != null) "Редактировать задачу" else "Создать задачу",
            fontSize = 24.sp
        )

        //Редактирование (или создание) название
        OutlinedTextField(
            value = title.value,
            onValueChange = { title.value = it },
            label = { Text("Название") },
            singleLine = true
        )

        //Описание
        OutlinedTextField(
            value = description.value,
            onValueChange = { description.value = it },
            label = { Text("Описание") },
            maxLines = 4
        )

        //Указание приоритета
        Row(verticalAlignment = Alignment.CenterVertically) {
            for (i in 1..3) {
                val isActive = priority.value == i
                val backgroundColor = when (i) {
                    1 -> if (isActive) Color.Green else Color.Transparent
                    2 -> if (isActive) Color.Yellow else Color.Transparent
                    3 -> if (isActive) Color.Red else Color.Transparent
                    else -> Color.Transparent
                }

                Button(
                    onClick = { priority.value = i },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = backgroundColor
                    ),
                    border = BorderStroke(1.dp, Color.Black),
                    modifier = Modifier.padding(horizontal = 4.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                ) {
                    Text("$i", color = Color.Black)
                }
            }
        }
        //Флаг "Важности"
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isImportantFlag.value,
                onCheckedChange = { isImportantFlag.value = it }
            )
            Text("Флаг задачи")
        }

        //Запарный ввод даты
        val dateError = remember { mutableStateOf(false) }
        OutlinedTextField(
            value = expDate.value,
            onValueChange = {
                expDate.value = it
                dateError.value = false
            },
            label = { Text("Дата и время (yyyy-MM-dd HH:mm)") },
            singleLine = true,
            isError = dateError.value
        )

        Spacer(modifier = Modifier.height(16.dp))

        //Парсинг даты
        Button(
            onClick = {
                if (title.value.isNotBlank()) {

                    var parsedDate: LocalDateTime? = null
                    if (expDate.value.isNotBlank()) {
                        try {
                            parsedDate = LocalDateTime.parse(expDate.value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                        } catch (e: DateTimeParseException) {
                            dateError.value = true
                            return@Button
                        }
                    }
                    // Пересоздаём (или создаем) задачу
                    val newTask = Task(
                        title = title.value,
                        description = description.value,
                        priority = priority.value,
                        isImportantFlag = isImportantFlag.value,
                        expDate = parsedDate,
                        isDone = existingTask?.isDone ?: mutableStateOf(false)
                    )
                    if (existingTask != null && taskIndex != null) {
                        taskList[taskIndex] = newTask // Случай редактирования
                    } else {
                        taskList.add(newTask) // Случай добавления новой задачи
                    }
                    onBack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сохранить")
        }
    }
}
