package com.example.appvacaciones.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface LugarDao {

    @Query("SELECT * FROM Lugar ORDER BY realizada")
    fun findAll(): List<Lugar>

    @Query("SELECT COUNT(*) FROM Lugar")
    fun contar(): Int

    @Insert
    fun insertar(lugar: Lugar): Long

    @Update
    fun actualizar(lugar: Lugar)

    @Delete
    fun eliminar(lugar: Lugar)

    @Query("SELECT * FROM Lugar ORDER BY realizada")
    fun getAllLugares(): LiveData<List<Lugar>>
}
