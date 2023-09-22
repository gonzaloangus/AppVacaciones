package com.example.appvacaciones.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Lugar::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun lugarDao(): LugarDao

    companion object {
        @Volatile
        private var BASE_DATOS: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return BASE_DATOS ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Lugar.bd"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { BASE_DATOS = it }
            }
        }
    }
}
