package fr.droidfactory.cosmo.sdk.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import fr.droidfactory.cosmo.sdk.data.database.entities.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ProductsDao {

    @Query("SELECT * FROM PRODUCTS")
    suspend fun getAllProducts(): List<ProductEntity>

    @Query("SELECT * FROM PRODUCTS WHERE mac_address = :macAddress")
    suspend fun getProductByMacAddress(macAddress: String): ProductEntity?

    @Query("SELECT * FROM PRODUCTS")
    fun observeProducts(): Flow<List<ProductEntity>>

    @Query("DELETE FROM PRODUCTS")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(product: ProductEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(products: List<ProductEntity>): List<Long>

    @Update
    suspend fun update(product: ProductEntity)

    @Update
    suspend fun updateAll(product: List<ProductEntity>)

    @Transaction
    suspend fun upsert(product: ProductEntity) {
        val id = insert(product)
        if(id == -1L) {
            update(product)
        }
    }

    @Transaction
    suspend fun upsertAll(products: List<ProductEntity>) {
        val ids = insertAll(products)
        val updateList = ids.indices
            .asSequence()
            .filter { index -> ids[index] == -1L }
            .map { index -> products[index] }
            .toList()
        if (updateList.isNotEmpty()) {
            updateAll(updateList)
        }
    }
}