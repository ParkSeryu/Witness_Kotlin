package com.parkseryu.witness.repository

import android.content.Context
import com.parkseryu.witness.db.AppDatabase
import com.parkseryu.witness.dto.AnniversaryEntity
import com.parkseryu.witness.dto.MeetDayEntity
import com.parkseryu.witness.dto.UserAnniversaryEntity

interface DayRepository{
    fun selectMeetDay() : List<MeetDayEntity>
    fun selectAnniversaryDay() : List<AnniversaryEntity>
    fun selectUserAnniversaryDay() : List<UserAnniversaryEntity>
    fun selectUnionAnniversaryDay() : List<AnniversaryEntity>
    fun insert(meetDayEntity: MeetDayEntity)
    fun insert(anniversaryEntity: AnniversaryEntity)
    fun insert(userAnniversaryEntity: UserAnniversaryEntity)
    fun updateAnniversary(updateLeftDay: String, originalLeftDay : String)
    fun updateUserAnniversary(updateLeftDay: String, originalLeftDay : String)
}

class DayRepositoryImpl(context: Context) : DayRepository{
    private val meetDayDao = AppDatabase.getInstance(context).meetDayDao()
    private val anniversaryDao = AppDatabase.getInstance(context).anniversaryDao()
    private val userAnniversaryDao = AppDatabase.getInstance(context).userAnniversaryDao()

    override fun selectMeetDay() : List<MeetDayEntity>{
        return meetDayDao.getAll()
    }

    override fun selectAnniversaryDay(): List<AnniversaryEntity> {
        return anniversaryDao.getAll()
    }

    override fun selectUserAnniversaryDay(): List<UserAnniversaryEntity> {
        return userAnniversaryDao.getAll()
    }

    override fun selectUnionAnniversaryDay(): List<AnniversaryEntity> {
        return anniversaryDao.getUnionTable()
    }

    override fun insert(meetDayEntity: MeetDayEntity) {
        meetDayDao.insert(meetDayEntity)
    }

    override fun insert(anniversaryEntity: AnniversaryEntity) {
        anniversaryDao.insert(anniversaryEntity)
    }

    override fun insert(userAnniversaryEntity: UserAnniversaryEntity) {
        userAnniversaryDao.insert(userAnniversaryEntity)
    }

    override fun updateAnniversary(updateLeftDay: String, originalLeftDay : String) {
        anniversaryDao.update(updateLeftDay, originalLeftDay)
    }

    override fun updateUserAnniversary(updateLeftDay: String, originalLeftDay : String) {
        userAnniversaryDao.update(updateLeftDay, originalLeftDay)
    }
}