package com.example.data.source.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {

    @Query("SELECT * FROM card_history_table ORDER BY uid DESC")
    fun getAllItems(): Flow<List<CardEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOne(card: CardEntity)

    @Delete
    suspend fun deleteOne(card: CardEntity)
}