package com.theappland.veroandroidtaskapp.repository

import com.google.gson.JsonArray
import com.theappland.veroandroidtaskapp.api.TaskApi
import com.theappland.veroandroidtaskapp.db.TaskDao
import com.theappland.veroandroidtaskapp.model.Task
import com.theappland.veroandroidtaskapp.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskApi: TaskApi,
    private val taskDao: TaskDao
) : TaskRepositoryInterface {

    override suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    override suspend fun getTasks() : Resource<JsonArray> {
        val response = try {
            taskApi.getTasks()
        } catch(e: Exception) {
            return Resource.Error("Task Error! " + (e.localizedMessage ?: ""))
        }
        return Resource.Success(response)
    }

    override suspend fun getAllTasks(): List<Task> {
        return taskDao.getAllTasks()
    }

    override suspend fun deleteAllTasks() {
        taskDao.deleteAllTasks()
    }
}