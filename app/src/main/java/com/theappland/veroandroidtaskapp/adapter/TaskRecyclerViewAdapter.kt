package com.theappland.veroandroidtaskapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.theappland.veroandroidtaskapp.databinding.TaskRowBinding
import com.theappland.veroandroidtaskapp.model.Task
import java.util.*
import kotlin.collections.ArrayList

class TaskRecyclerViewAdapter(private val taskList: ArrayList<Task>): RecyclerView.Adapter<TaskRecyclerViewAdapter.TaskViewHolder>(), Filterable {

    var taskFilterList = ArrayList<Task>()

    init {
        taskFilterList = taskList
    }

    inner class TaskViewHolder(val binding: TaskRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(TaskRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskFilterList[position]
        holder.binding.apply {
            if (task.colorCode == "") {
                taskMaterialCardView.setCardBackgroundColor(Color.WHITE)
                taskColorCodeTextView.text = "Task Color Code: #ffffff"
            } else {
                taskMaterialCardView.setCardBackgroundColor(Color.parseColor(task.colorCode))
                taskColorCodeTextView.text = "Task Color Code: " + task.colorCode
            }
            taskTextView.text = "Task: " + task.task
            taskTitleTextView.text = "Task Title: " + task.title
            taskDescriptionTextView.text = "Task Description: " + task.description
        }
    }

    override fun getItemCount(): Int {
        return taskFilterList.size
    }

    fun updateTasks(newTasks: List<Task>) {
        taskList.clear()
        taskList.addAll(newTasks)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charSearch = p0.toString()
                if (charSearch.isEmpty()) {
                    taskFilterList = taskList
                } else {
                    val resultList = ArrayList<Task>()
                    for (row in taskList) {
                        if (row.task.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT)) ||
                            row.title.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT)) ||
                            row.description.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT)) ||
                            row.colorCode.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT)) ||
                            row.BusinessUnitKey!!.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT)) ||
                            row.businessUnit.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT)) ||
                            row.parentTaskID.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT)) ||
                            row.preplanningBoardQuickSelect!!.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT)) ||
                            row.sort.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT)) ||
                            row.wageType.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT)) ||
                            row.workingTime!!.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    taskFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = taskFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                p1?.let {
                    taskFilterList = it.values as ArrayList<Task>
                    notifyDataSetChanged()
                }
            }
        }
    }
}