package com.panassevich.panassevich.shared.loans.core.data.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.panassevich.panassevich.shared.loans.core.data.db.model.LoanDbModel

@Database(
    entities = [LoanDbModel::class],
    version = 1,
    exportSchema = false
)
abstract class LoansDatabase : RoomDatabase() {

    abstract fun loansDao(): LoansDao

    companion object {
        private var INSTANCE: LoansDatabase? = null
        private const val DATABASE_NAME = "main.db"

        fun getInstance(application: Application): LoansDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    application,
                    LoansDatabase::class.java,
                    DATABASE_NAME
                ).build().also { INSTANCE = it }
            }
    }
}