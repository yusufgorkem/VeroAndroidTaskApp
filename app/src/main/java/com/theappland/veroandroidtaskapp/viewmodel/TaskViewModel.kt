package com.theappland.veroandroidtaskapp.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.theappland.veroandroidtaskapp.model.Task
import com.theappland.veroandroidtaskapp.repository.TaskRepositoryInterface
import com.theappland.veroandroidtaskapp.util.CustomSharedPreferences
import com.theappland.veroandroidtaskapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepositoryInterface
): ViewModel() {

    var tasks = MutableLiveData<List<Task>>()
    private val taskList = arrayListOf<Task>()

    var isLoading = MutableLiveData<Boolean>()
    var dataLoading = MutableLiveData<Boolean>()

    private lateinit var customSharedPreferences : CustomSharedPreferences
    private var refreshTime = 60 * 60 * 1000 * 1000L * 1000L

    fun getTaskData(context: Context) {
        customSharedPreferences = CustomSharedPreferences(context)
        val updateTime = customSharedPreferences.getTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            getAllTasks(context)
        } else {
            getTasks(context)
        }
    }

    fun getFromApi(context: Context) {
        getTasks(context)
    }

    private fun getTasks(context: Context) {
        viewModelScope.launch {
            dataLoading.value = true
            when(val result = taskRepository.getTasks()) {
                is Resource.Success -> {
                    result.data?.let {
                        taskList.clear()
                        deleteAllTasks()
                        val gson = GsonBuilder().create()
                        var i = 0
                        while (i < it.size()) {
                            val task = gson.fromJson(it[i],Task::class.java)
                            if (task.preplanningBoardQuickSelect == null) {
                                task.preplanningBoardQuickSelect = ""
                            }
                            if (task.BusinessUnitKey == null) {
                                task.BusinessUnitKey = ""
                            }
                            if (task.workingTime == null) {
                                task.workingTime = ""
                            }
                            taskList.add(task)
                            insertTask(task)
                            i++
                        }
                        tasks.value = taskList
                        Toast.makeText(context,"Tasks From API",Toast.LENGTH_SHORT).show()
                        dataLoading.value = false
                    }
                }

                is Resource.Error -> {
                    println(result.message!!)
                    dataLoading.value = false
                }
            }
        }
    }

    private fun insertTask(task: Task) = viewModelScope.launch {
        taskRepository.insertTask(task)
        customSharedPreferences.saveTime(System.nanoTime())
    }

    private fun getAllTasks(context: Context) = viewModelScope.launch {
        taskList.clear()
        taskList.addAll(taskRepository.getAllTasks())
        tasks.value = taskList
        Toast.makeText(context,"Tasks From Room Database",Toast.LENGTH_SHORT).show()
    }

    private fun deleteAllTasks() = viewModelScope.launch {
        taskRepository.deleteAllTasks()
    }
}