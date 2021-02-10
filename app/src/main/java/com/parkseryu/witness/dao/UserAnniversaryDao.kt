package com.parkseryu.witness.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.parkseryu.witness.dto.AnniversaryEntity
import com.parkseryu.witness.dto.UserAnniversaryEntity

@Dao
interface UserAnniversaryDao {
    @Insert
    fun insert(userAnniversaryEntity: UserAnniversaryEntity)

    @Query("SELECT * FROM USER_ANNIVERSARY")
    fun getAll():List<UserAnniversaryEntity>

    @Query("UPDATE ANNIVERSARY SET LEFTDAY=:updateLeftDay WHERE LEFTDAY=:originalLeftDay")
    fun update(updateLeftDay : String, originalLeftDay : String)


}

