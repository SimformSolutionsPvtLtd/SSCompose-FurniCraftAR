package com.simform.ssfurnicraftar.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.simform.ssfurnicraftar.data.local.database.model.ModelEntity

/**
 * DAO for [ModelEntity] access
 */
@Dao
interface ModelDao {

    /**
     * Insert or update models
     */
    @Upsert
    suspend fun upsertModels(models: List<ModelEntity>)

    /**
     * Get all models
     */
    @Query(value = "SELECT * FROM model")
    suspend fun getAllModels(): List<ModelEntity>

    /**
     * Delete all models
     */
    @Query(value = "DELETE FROM model")
    suspend fun deleteAllModels()
}
