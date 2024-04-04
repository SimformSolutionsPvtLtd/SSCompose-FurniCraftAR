package com.simform.ssfurnicraftar.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.simform.ssfurnicraftar.data.local.database.model.ProductEntity

/**
 * DAO for [ProductEntity] access
 */
@Dao
interface ProductDao {

    /**
     * Insert or update models
     */
    @Upsert
    suspend fun upsertProducts(models: List<ProductEntity>)

    /**
     * Get all models
     */
    @Query(value = "SELECT * FROM product")
    suspend fun getAllProducts(): List<ProductEntity>

    /**
     * Delete all models
     */
    @Query(value = "DELETE FROM product")
    suspend fun deleteAllProducts()
}
