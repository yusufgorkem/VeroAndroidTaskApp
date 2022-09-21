package com.theappland.veroandroidtaskapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    var BusinessUnitKey: String?,
    val businessUnit: String,
    val colorCode: String,
    val description: String,
    val parentTaskID: String,
    var preplanningBoardQuickSelect: String?,
    val sort: String,
    val task: String,
    val title: String,
    val wageType: String,
    var workingTime: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}