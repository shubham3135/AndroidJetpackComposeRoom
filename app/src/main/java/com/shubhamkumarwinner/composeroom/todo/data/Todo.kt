package com.shubhamkumarwinner.composeroom.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todoTable")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    var title: String,
    var description: String
)
