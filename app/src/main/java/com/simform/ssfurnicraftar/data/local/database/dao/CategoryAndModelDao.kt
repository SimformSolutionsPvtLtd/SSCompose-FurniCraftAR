package com.simform.ssfurnicraftar.data.local.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.simform.ssfurnicraftar.data.local.database.model.CategoryEntity
import com.simform.ssfurnicraftar.data.local.database.model.CategoryModelCrossRef
import com.simform.ssfurnicraftar.data.local.database.model.ModelEntity
import com.simform.ssfurnicraftar.data.model.Category

/**
 * DAO compositing [ModelDao] and [CategoryDao]
 */
@Dao
abstract class CategoryAndModelDao : ModelDao, CategoryDao {

    /**
     * Create or update category and model ref
     */
    @Upsert
    abstract suspend fun upsertCategoryModelCrossRefs(ref: List<CategoryModelCrossRef>)

    /**
     * Insert category with models
     *
     * @param category The category to insert
     * @param models The models corresponding to [category]
     */
    @Transaction
    open suspend fun insertCategoryWithModels(category: CategoryEntity, models: List<ModelEntity>) {
        val categoryId = findCategory(category.category)?.id ?: insertCategory(category)

        upsertModels(models)
        val categoryModelRefs = models.map { CategoryModelCrossRef(categoryId, it.id) }
        upsertCategoryModelCrossRefs(categoryModelRefs)
    }

    /**
     * Get all the models of given [category].
     *
     * This query uses [CategoryModelCrossRef] table and perform joint operation
     * to retrieve models for particular category.
     */
    @Transaction
    @Query(
        value = """
        SELECT model.* FROM model
        LEFT JOIN category_model ON modelId = model.id
        LEFT JOIN category ON category_model.categoryId = category.id
        WHERE category.category = :category
    """
    )
    abstract fun getModelsByCategory(category: Category): PagingSource<Int, ModelEntity>

    /**
     * Delete all categories and models
     */
    @Transaction
    open suspend fun deleteAllCategoriesAndModels() {
        deleteAllModels()
        deleteAllCategories()
    }

    /**
     * Delete models by particular category.
     *
     * This query uses [CategoryModelCrossRef] table and perform joint operation
     * to retrieve models for particular category and performs delete operation
     * of that models.
     */
    @Transaction
    @Query(
        value = """
        DELETE FROM model WHERE id IN (
            SELECT model.id FROM model
            LEFT JOIN category_model ON category_model.modelId = model.id
            LEFT JOIN category ON category_model.categoryId = category.id
            WHERE category.category = :category
        )
    """
    )
    abstract suspend fun deleteModelsByCategory(category: Category)
}
