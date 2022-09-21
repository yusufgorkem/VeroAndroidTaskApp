package com.theappland.veroandroidtaskapp.db

import androidx.room.*
import com.theappland.veroandroidtaskapp.model.Task

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Query("SELECT * FROM task")
    suspend fun getAllTasks() : List<Task>

    @Query("DELETE FROM task")
    suspend fun deleteAllTasks()
}