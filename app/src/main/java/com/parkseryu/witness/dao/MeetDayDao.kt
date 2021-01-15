package com.parkseryu.witness.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface MeetDayDao {
    @Query("SELECT * FROM MEETDAY")
    fun getAll() : List<String>
}