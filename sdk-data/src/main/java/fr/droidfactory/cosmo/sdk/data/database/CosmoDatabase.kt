package fr.droidfactory.cosmo.sdk.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.droidfactory.cosmo.sdk.data.database.daos.ProductsDao
import fr.droidfactory.cosmo.sdk.data.database.entities.ProductEntity

@Database(
    entities = [
        ProductEntity::class
    ],
    version = 1
)
internal abstract class CosmoDatabase: RoomDatabase() {
    abstract fun productsDao(): ProductsDao

    internal companion object {
        const val DATABASE_NAME = "Cosmo-app.db"
    }
}