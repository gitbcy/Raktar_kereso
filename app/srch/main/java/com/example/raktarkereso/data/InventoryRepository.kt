package com.example.raktarkereso.data

import androidx.lifecycle.LiveData

/**
 * Repository layer: the single source of truth for inventory data.
 * ViewModels talk to this class instead of touching the DAO directly,
 * which keeps the persistence mechanism (Room/SQLite) swappable later.
 */
class InventoryRepository(private val dao: InventoryDao) {

    suspend fun insert(item: InventoryItem) {
        dao.insert(item)
    }

    suspend fun search(query: String): List<InventoryItem> {
        return dao.search(query)
    }

    fun getAllItems(): LiveData<List<InventoryItem>> {
        return dao.getAllItems()
    }
}
