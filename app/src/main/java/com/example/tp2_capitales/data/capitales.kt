package com.example.tp2_capitales.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Capital(
    @PrimaryKey val capitalid: Int,
    @ColumnInfo(name = "nombre_capital") val nombreCapital: String?,
    @ColumnInfo(name = "pais_origen") val paisOrigen: String?,
    @ColumnInfo(name = "cantidad_poblacion") val cantidad_poblacion: Int?
)