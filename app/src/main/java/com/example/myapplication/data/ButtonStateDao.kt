package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ButtonStateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(buttonStateEntity: ButtonStateEntity)

    @Update
    suspend fun update(exampleEntity: ButtonStateEntity)

    @Query("SELECT * FROM button_state WHERE id = :id")
    fun getById(id: Int): ButtonStateEntity
}
