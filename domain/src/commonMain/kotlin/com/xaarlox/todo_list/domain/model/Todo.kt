package com.xaarlox.todo_list.domain.model

data class Todo(
    val id: Int? = null,
    val title: String,
    val description: String?,
    val isDone: Boolean = false
)