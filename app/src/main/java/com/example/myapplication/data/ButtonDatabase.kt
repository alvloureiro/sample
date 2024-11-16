package com.example.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ButtonStateEntity::class], version = 1)
abstract class ButtonDatabase : RoomDatabase() {
    abstract fun buttonDao(): ButtonStateDao

    companion object {
        @Volatile
        private var INSTANCE: ButtonDatabase? = null

        fun getDatabase(context: Context): ButtonDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ButtonDatabase::class.java,
                    "button.db"
                )
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
