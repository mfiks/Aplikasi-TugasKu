package com.example.tugasku.database

import android.content.Context
import androidx.room.Room

private const val dbName = "ToDoListTugasKu"

object DatabaseClient {
    fun getService(context: Context): DatabaseService{
        return Room.databaseBuilder(
            context,
            DatabaseService::class.java,
            dbName
        ).build()
    }
}