package com.xaarlox.todo_list.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xaarlox.todo_list.data.local.entity.TodoEntity

@Database(
    entities = [TodoEntity::class], version = 1, exportSchema = false
)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}