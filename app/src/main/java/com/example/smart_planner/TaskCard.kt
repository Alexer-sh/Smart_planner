package com.example.smart_planner

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.format.DateTimeFormatter

@Composable
fun TaskCard(task: Task, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Название задачи
                Text(task.title, fontSize = 20.sp, maxLines = 1)

                // Описание задачи
                if (!task.description.isNullOrEmpty()) {
                    Text(task.description, fontSize = 14.sp, maxLines = 2, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Приоритет
                Row(verticalAlignment = Alignment.CenterVertically) {
                    for (i in 1..3) {
                        val color = when (task.priority) {
                            1 -> if (i == 1) Color.Green else Color.Gray
                            2 -> if (i <= 2) Color.Yellow else Color.Gray
                            3 -> Color.Red
                            else -> Color.Gray
                        }

                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .padding(end = 4.dp)
                                .background(color = color, shape = CircleShape)
                        )
                    }
                }
            }
            // Дата (если есть)
            if (task.expDate != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Сделать до: ${task.expDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Флаг задачи
            if (task.isImportantFlag) {
                Text("❗", fontSize = 24.sp, modifier = Modifier.padding(start = 8.dp))
            }

            // Галочка выполнения
            Checkbox(
                checked = task.isDone.value,
                onCheckedChange = { checked -> task.isDone.value = checked }
            )
        }
    }
}
