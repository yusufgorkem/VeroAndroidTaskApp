package com.theappland.veroandroidtaskapp.repository

import com.google.gson.JsonArray
import com.theappland.veroandroidtaskapp.model.Task
import com.theappland.veroandroidtaskapp.util.Resource

interface TaskRepositoryInterface {

    suspend fun insertTask(task: Task)

    suspend fun getTasks() : Resource<JsonArray>

    suspend fun getAllTasks() : List<Task>

    suspend fun deleteAllTasks()
}