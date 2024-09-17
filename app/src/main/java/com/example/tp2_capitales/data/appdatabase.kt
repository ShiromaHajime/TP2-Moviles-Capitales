package com.example.tp2_capitales.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Capital::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun capitalDao(): CapitalDao
}