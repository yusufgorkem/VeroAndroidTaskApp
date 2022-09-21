package com.theappland.veroandroidtaskapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.theappland.veroandroidtaskapp.model.Task

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao() : TaskDao
}