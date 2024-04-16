package com.simform.ssfurnicraftar.data.local.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.simform.ssfurnicraftar.data.local.database.model.CategoryEntity
import com.simform.ssfurnicraftar.data.local.database.model.CategoryProductCrossRef
import com.simform.ssfurnicraftar.data.local.database.model.ProductEntity
import com.simform.ssfurnicraftar.data.model.Category

/**
 * DAO compositing [ProductDao] and [CategoryDao]
 */
@Dao
abstract class CategoryAndProductDao : ProductDao, CategoryDao {

    /**
     * Create or update category and product ref
     */
    @Upsert
    abstract suspend fun upsertCategoryProductCrossRefs(ref: List<CategoryProductCrossRef>)

    /**
     * Insert category with products
     *
     * @param category The category to insert
     * @param products The products corresponding to [category]
     */
    @Transaction
    open suspend fun insertCategoryWithProducts(
        category: CategoryEntity,
        products: List<ProductEntity>
    ) {
        val categoryId = findCategory(category.category)?.id ?: insertCategory(category)

        upsertProducts(products)
        val categoryProductRefs = products.map { CategoryProductCrossRef(categoryId, it.id) }
        upsertCategoryProductCrossRefs(categoryProductRefs)
    }

    /**
     * Get all the products of given [category].
     *
     * This query uses [CategoryProductCrossRef] table and perform joint operation
     * to retrieve products for particular category.
     */
    @Transaction
    @Query(
        value = """
        SELECT product.* FROM product
        LEFT JOIN category_product ON productId = product.id
        LEFT JOIN category ON category_product.categoryId = category.id
        WHERE category.category = :category
    """
    )
    abstract fun getProductsByCategory(category: Category): PagingSource<Int, ProductEntity>

    /**
     * Get category for given [productId].
     *
     * This query uses [CategoryProductCrossRef] table and perform joint operation
     * to retrieve category for particular product.
     */
    @Transaction
    @Query(
        value = """
            SELECT category.* FROM category
            LEFT JOIN category_product ON categoryId = category.id
            WHERE productId = :productId
        """
    )
    abstract suspend fun getCategoryByProductId(productId: String): CategoryEntity?

    /**
     * Delete all categories and products
     */
    @Transaction
    open suspend fun deleteAllCategoriesAndProducts() {
        deleteAllProducts()
        deleteAllCategories()
    }

    /**
     * Delete products by particular category.
     *
     * This query uses [CategoryProductCrossRef] table and perform joint operation
     * to retrieve products for particular category and performs delete operation
     * of that products.
     */
    @Transaction
    @Query(
        value = """
        DELETE FROM product WHERE id IN (
            SELECT product.id FROM product
            LEFT JOIN category_product ON category_product.productId = product.id
            LEFT JOIN category ON category_product.categoryId = category.id
            WHERE category.category = :category
        )
    """
    )
    abstract suspend fun deleteProductsByCategory(category: Category)
}
