package com.example.tp2_capitales.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.Delete

@Dao
interface CapitalDao {
    @Query("SELECT * FROM capital")
    fun getAll(): List<Capital>

    @Query("SELECT * FROM capital WHERE capitalid IN (:capitalIds)")
    fun loadAllByIds(capitalIds: IntArray): List<Capital>

    @Query("SELECT * FROM capital WHERE nombre_capital LIKE :nombre LIMIT 1")
    fun findByName(nombre: String): Capital

    @Insert
    fun insertAll(vararg users: Capital)

    @Query("UPDATE capital SET cantidad_poblacion = :nuevaPoblacion WHERE nombre_capital = :nombre")
    fun updatePopulationByName(nombre: String, nuevaPoblacion: Int)

    @Query("DELETE FROM capital WHERE nombre_capital = :nombre")
    fun deleteByName(nombre: String)

    @Query("DELETE FROM capital WHERE pais_origen = :pais")
    fun deleteAllByCountry(pais: String)
}