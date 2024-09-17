package com.example.tp2_capitales.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface CapitalDao {
    @Query("SELECT * FROM capital")
    fun getAll(): Flow<List<Capital>>

    @Query("SELECT * FROM capital WHERE capitalid IN (:capitalIds)")
    fun loadAllByIds(capitalIds: IntArray): Flow<List<Capital>>

    @Query("SELECT * FROM capital WHERE nombre_capital = :nombre")
    fun findByName(nombre: String): Flow<List<Capital>>

    @Query("SELECT * FROM capital WHERE pais_origen = :pais")
    fun loadAllByCountry(pais: String): Flow<List<Capital>>

    @Insert
    suspend fun insertAll(vararg capitals: Capital)

    @Query("UPDATE capital SET cantidad_poblacion = :nuevaPoblacion WHERE nombre_capital = :nombre")
    suspend fun updatePopulationByName(nombre: String, nuevaPoblacion: Int)

    @Query("DELETE FROM capital WHERE nombre_capital = :nombre")
    suspend fun deleteByName(nombre: String)

    @Query("DELETE FROM capital WHERE pais_origen = :pais")
    suspend fun deleteAllByCountry(pais: String)
}
