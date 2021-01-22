package com.parkseryu.witness.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.parkseryu.witness.dto.MeetDay

@Dao
interface MeetDayDao {
    @Query("SELECT * FROM MEETDAY")
    fun getAll() : List<MeetDay>
}