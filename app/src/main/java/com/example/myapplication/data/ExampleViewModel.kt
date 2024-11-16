package com.example.myapplication.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ExampleViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ButtonRepository

    init {
        val dao = ButtonDatabase.getDatabase(application).buttonDao()
        repository = ButtonRepository(dao)
    }

    fun insert(exampleEntity: ButtonStateEntity) = viewModelScope.launch {
        Log.d("ExampleViewModel", "insert: $exampleEntity")
        repository.insert(exampleEntity)
    }

    fun update(exampleEntity: ButtonStateEntity) = viewModelScope.launch {
        Log.d("ExampleViewModel", "update: $exampleEntity")
        repository.update(exampleEntity)
    }
}