package com.example.raktarkereso.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a single product stored in a specific
 * warehouse and storage unit. Multiple items can share the same
 * warehouse/storage-unit combination (a storage unit can hold many products).
 */
@Entity(tableName = "inventory_items")
data class InventoryItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // Name or ID of the product, exactly as entered by the user
    val productName: String,

    // e.g. "II. raktár"
    val warehouseName: String,

    // e.g. "Középső jobb fent"
    val storageUnitName: String,

    // Timestamp used only for ordering; not shown to the user
    val createdAt: Long = System.currentTimeMillis()
)
