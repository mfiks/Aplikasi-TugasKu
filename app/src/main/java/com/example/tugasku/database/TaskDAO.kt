package com.example.tugasku.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDAO {

    @Update
    fun update(taskModel: TaskModel)

    @Delete
    fun delete(taskModel: TaskModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(taskModel: TaskModel)

    @Query("SELECT * FROM tableTask WHERE completed=:completed")
    fun taskAll(completed: Boolean): LiveData<List<TaskModel>>

    @Query("SELECT * FROM tableTask WHERE completed=:completed AND date=:date")
    fun taskAll(completed: Boolean, date: Long): LiveData<List<TaskModel>>

    @Query("DELETE FROM tableTask WHERE completed=1 ")
    fun deleteCompleted()

    @Query("DELETE FROM tableTask")
    fun deleteAll()

}