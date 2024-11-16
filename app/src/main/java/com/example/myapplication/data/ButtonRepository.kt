package com.example.myapplication.data

import android.util.Log

class ButtonRepository(private val buttonStateDao: ButtonStateDao) {
    fun getById(id: Int) = buttonStateDao.getById(id)

    suspend fun insert(entity: ButtonStateEntity) {
        Log.d("ButtonRepository", "insert: $entity")
        buttonStateDao.insert(entity)
    }

    suspend fun update(exampleEntity: ButtonStateEntity) {
        Log.d("ButtonRepository", "update: $exampleEntity")
        buttonStateDao.update(exampleEntity)
    }
}
