package com.example.smart_planner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Просто примеры, чтобы тестить было быстрее
            val tasksList = remember { mutableStateListOf(
                Task("Купить продукты", "Молоко, хлеб, яйца", 2),
                Task("Сделать домашку", "Compose tutorial", 3)
            )}
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "start") {
                // Отправляет к стартовому экрану
                composable("start") {
                    StartScreen(navController)
                }
                // К списку задач
                composable("tasks") {
                    TasksScreen(
                        tasksList = tasksList,
                        onAddTask = { navController.navigate("addEditTask") },
                        onEditTask = { index -> navController.navigate("addEditTask/$index") }
                    )
                }
                // К созданию задач
                composable("addEditTask") {
                    AddEditTaskScreen(
                        taskList = tasksList,
                        onBack = { navController.popBackStack() }
                    )
                }
                // К редактированию задач
                composable(
                    "addEditTask/{taskIndex}",
                    arguments = listOf(navArgument("taskIndex") { type = NavType.IntType })
                ) { backStackEntry ->
                    val index = backStackEntry.arguments?.getInt("taskIndex") ?: 0
                    AddEditTaskScreen(
                        taskList = tasksList,
                        existingTask = tasksList[index],
                        taskIndex = index,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
