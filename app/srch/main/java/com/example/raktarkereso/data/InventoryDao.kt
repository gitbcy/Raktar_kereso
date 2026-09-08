package com.example.raktarkereso.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface InventoryDao {

    @Insert
    suspend fun insert(item: InventoryItem)

    /**
     * Case-insensitive partial match search on the product name.
     * Example: searching "csavar" will also find "Csavar M6" or "hatlapfejű csavar".
     */
    @Query(
        """
        SELECT * FROM inventory_items 
        WHERE productName LIKE '%' || :query || '%' COLLATE NOCASE
        ORDER BY productName ASC
        """
    )
    suspend fun search(query: String): List<InventoryItem>

    // Useful for a future "all items" screen; kept for completeness / debugging.
    @Query("SELECT * FROM inventory_items ORDER BY createdAt DESC")
    fun getAllItems(): LiveData<List<InventoryItem>>
}
