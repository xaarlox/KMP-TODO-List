package com.xaarlox.todo_list

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.xaarlox.todo_list.data.di.dataModule
import com.xaarlox.todo_list.ui.di.uiModule
import com.xaarlox.todo_list.ui.navigation.TodoNavHost
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.core.module.Module

@Composable
@Preview
fun App(
    platformModule: Module = Module()
) {
    KoinApplication(
        application = {
            modules(platformModule, uiModule, dataModule)
        }
    ) {
        MaterialTheme {
            TodoNavHost()
        }
    }
}