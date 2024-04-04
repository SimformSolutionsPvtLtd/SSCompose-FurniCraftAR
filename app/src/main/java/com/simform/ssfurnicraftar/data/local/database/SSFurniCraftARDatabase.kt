package com.simform.ssfurnicraftar.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.simform.ssfurnicraftar.data.local.database.converter.Converters
import com.simform.ssfurnicraftar.data.local.database.dao.CategoryAndProductDao
import com.simform.ssfurnicraftar.data.local.database.dao.ProductDao
import com.simform.ssfurnicraftar.data.local.database.dao.RemoteKeyDao
import com.simform.ssfurnicraftar.data.local.database.model.CategoryEntity
import com.simform.ssfurnicraftar.data.local.database.model.CategoryProductCrossRef
import com.simform.ssfurnicraftar.data.local.database.model.ProductEntity
import com.simform.ssfurnicraftar.data.local.database.model.RemoteKey

@Database(
    entities = [
        ProductEntity::class,
        RemoteKey::class,
        CategoryEntity::class,
        CategoryProductCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SSFurniCraftARDatabase : RoomDatabase() {

    abstract fun modelDao(): ProductDao

    abstract fun remoteKeyDao(): RemoteKeyDao

    abstract fun categoryAndProductDao(): CategoryAndProductDao
}
