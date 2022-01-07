package com.example.tugasku.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [TaskModel::class],
    exportSchema = false,
    version = 1
)
abstract class DatabaseService: RoomDatabase(){
    abstract fun taskDAO(): TaskDAO
}