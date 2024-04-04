package com.simform.ssfurnicraftar.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.simform.ssfurnicraftar.data.local.database.model.RemoteKey

/**
 * DAO for [RemoteKey] access. Remote keys are used for pagination.
 */
@Dao
interface RemoteKeyDao {

    /**
     * Insert or update remote keys
     */
    @Upsert
    suspend fun upsertRemoteKeys(keys: List<RemoteKey>)

    /**
     * Get all remote keys
     */
    @Query(value = "SELECT * FROM remote_key")
    suspend fun getRemoteKeys(): List<RemoteKey>

    /**
     * Get [RemoteKey] by product given [productId]
     *
     * @param productId The product id used for finding remote key
     * @return
     */
    @Query(value = "SELECT * FROM remote_key WHERE productId = :productId")
    suspend fun getRemoteKeyByProductId(productId: String): RemoteKey

    /**
     * Delete [RemoteKey] by product given [productId]
     *
     * @param productId The product id used for deleting remote key
     */
    @Query(value = "DELETE FROM remote_key WHERE productId = :productId")
    suspend fun deleteRemoteKeyByProductId(productId: String)

    /**
     * Delete all remote keys
     */
    @Query(value = "DELETE FROM remote_key")
    suspend fun deleteAllRemoteKeys()
}
