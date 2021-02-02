package com.parkseryu.witness.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.parkseryu.witness.dto.AnniversaryEntity

@Dao
interface AnniversaryDao {
    @Insert
    fun insert(anniversaryEntity: AnniversaryEntity)

    @Query("SELECT * FROM ANNIVERSARY")
    fun getAll(): List<AnniversaryEntity>

    @Query("UPDATE ANNIVERSARY SET LEFTDAY=:updateLeftDay WHERE LEFTDAY=:originalLeftDay")
    fun update(updateLeftDay : String, originalLeftDay : String)
}