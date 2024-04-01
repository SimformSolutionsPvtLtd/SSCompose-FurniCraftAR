package com.simform.ssfurnicraftar.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.simform.ssfurnicraftar.data.local.database.model.CategoryEntity
import com.simform.ssfurnicraftar.data.model.Category

/**
 * DAO for [CategoryEntity] access
 */
@Dao
interface CategoryDao {

    /**
     * Insert categories
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategories(categories: List<CategoryEntity>): List<Long>

    /**
     * Insert single category
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: CategoryEntity): Long

    /**
     * Get all categories
     */
    @Query(value = "SELECT * FROM category")
    suspend fun getAllCategories(): List<CategoryEntity>

    /**
     * Find [CategoryEntity] by [Category]
     */
    @Query(value = "SELECT * FROM category WHERE category = :category")
    suspend fun findCategory(category: Category): CategoryEntity?

    /**
     * Delete all categories
     */
    @Query(value = "DELETE FROM category")
    suspend fun deleteAllCategories()

    /**
     * Delete single category
     */
    @Delete
    suspend fun deleteCategory(category: CategoryEntity)
}
