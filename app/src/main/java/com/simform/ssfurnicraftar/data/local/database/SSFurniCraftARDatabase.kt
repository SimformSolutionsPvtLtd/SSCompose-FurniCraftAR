package com.simform.ssfurnicraftar.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.simform.ssfurnicraftar.data.local.database.converter.Converters
import com.simform.ssfurnicraftar.data.local.database.dao.CategoryAndModelDao
import com.simform.ssfurnicraftar.data.local.database.dao.ModelDao
import com.simform.ssfurnicraftar.data.local.database.dao.RemoteKeyDao
import com.simform.ssfurnicraftar.data.local.database.model.CategoryEntity
import com.simform.ssfurnicraftar.data.local.database.model.CategoryModelCrossRef
import com.simform.ssfurnicraftar.data.local.database.model.ModelEntity
import com.simform.ssfurnicraftar.data.local.database.model.RemoteKey

@Database(
    entities = [
        ModelEntity::class,
        RemoteKey::class,
        CategoryEntity::class,
        CategoryModelCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SSFurniCraftARDatabase : RoomDatabase() {

    abstract fun modelDao(): ModelDao

    abstract fun remoteKeyDao(): RemoteKeyDao

    abstract fun categoryAndModelDao(): CategoryAndModelDao
}
