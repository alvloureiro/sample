package com.example.myapplication.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "button_state")
data class ButtonStateEntity(
    @PrimaryKey
    var id: Int,
    @ColumnInfo(name = "color")
    var color: Int
)
