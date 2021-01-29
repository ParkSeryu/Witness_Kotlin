package com.parkseryu.witness.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.parkseryu.witness.dto.MeetDayEntity

@Dao
interface MeetDayDao {
    @Insert
    fun insert(meetDayEntity: MeetDayEntity)

    @Query("SELECT * FROM MEETDAY")
    fun getAll(): List<MeetDayEntity>

}