package com.xaarlox.todo_list

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.xaarlox.todo_list.ui.navigation.TodoNavHost
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        TodoNavHost()
    }
}