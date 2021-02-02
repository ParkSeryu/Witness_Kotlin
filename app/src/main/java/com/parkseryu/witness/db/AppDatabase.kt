package com.parkseryu.witness.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.parkseryu.witness.dao.AnniversaryDao
import com.parkseryu.witness.dao.MeetDayDao
import com.parkseryu.witness.dto.AnniversaryEntity
import com.parkseryu.witness.dto.MeetDayEntity

@Database(entities = [MeetDayEntity::class, AnniversaryEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun meetDayDao(): MeetDayDao
    abstract fun anniversaryDao(): AnniversaryDao

    companion object {
        private const val DB_NAME = "room-db"
        private var instance: AppDatabase? = null


        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context)
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DB_NAME
            )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                    }
                }).fallbackToDestructiveMigration()
                .build()
        }


    }
}