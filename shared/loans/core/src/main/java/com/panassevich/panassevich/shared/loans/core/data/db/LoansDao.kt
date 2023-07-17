package com.panassevich.panassevich.shared.loans.core.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.panassevich.panassevich.shared.loans.core.data.db.model.LoanDbModel

@Dao
interface LoansDao {

    @Query("DELETE FROM loans")
    suspend fun removeAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: List<LoanDbModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: LoanDbModel)

    @Query("SELECT * FROM loans")
    suspend fun getAll(): List<LoanDbModel>

    @Transaction
    suspend fun removeAllAndInsert(data: List<LoanDbModel>) {
        removeAll()
        insert(data)
    }
}