package com.example.data.source.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "card_history_table"
)
data class CardEntity (
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "card") val card: String
)