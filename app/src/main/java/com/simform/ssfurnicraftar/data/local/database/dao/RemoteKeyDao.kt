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
     * Get [RemoteKey] by model given [modelID]
     *
     * @param modelID The model id used for finding remote key
     * @return
     */
    @Query(value = "SELECT * FROM remote_key WHERE modelID = :modelID")
    suspend fun getRemoteKeyByModelId(modelID: String): RemoteKey

    /**
     * Delete [RemoteKey] by model given [modelID]
     *
     * @param modelID The model id used for deleting remote key
     */
    @Query(value = "DELETE FROM remote_key WHERE modelID = :modelID")
    suspend fun deleteRemoteKeyByModelId(modelID: String)

    /**
     * Delete all remote keys
     */
    @Query(value = "DELETE FROM remote_key")
    suspend fun deleteAllRemoteKeys()
}
