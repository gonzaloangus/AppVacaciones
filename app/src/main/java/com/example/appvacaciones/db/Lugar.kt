package com.example.appvacaciones.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Lugar(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var nombre: String,
    var imagenRef: String,
    var latitud: Double,
    var longitud: Double,
    var orden: String,
    var costoAlojamiento: Int,
    var costoTraslados: Int,
    var comentarios: String,
    var realizada: Boolean = false
)
