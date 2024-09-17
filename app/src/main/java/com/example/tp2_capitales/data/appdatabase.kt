package com.example.tp2_capitales.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Capital::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun capitalDao(): CapitalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Definir migraciones, si tienes cambios de versión
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Aquí defines los cambios en el esquema, por ejemplo:
                // database.execSQL("ALTER TABLE capital ADD COLUMN nueva_columna INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "capital_database"
                )
                    .addMigrations(MIGRATION_1_2) // Añadir la migración
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
