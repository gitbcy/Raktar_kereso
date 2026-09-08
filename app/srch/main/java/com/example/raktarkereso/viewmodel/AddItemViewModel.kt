package com.example.raktarkereso.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.raktarkereso.data.InventoryItem
import com.example.raktarkereso.data.InventoryRepository
import kotlinx.coroutines.launch

/** Result of a save attempt, observed by the Add Item screen. */
sealed class SaveResult {
    object Success : SaveResult()
    object ValidationError : SaveResult()
}

/**
 * ViewModel for the "add new product" screen.
 * Performs basic input validation before writing to the database.
 */
class AddItemViewModel(private val repository: InventoryRepository) : ViewModel() {

    private val _saveResult = MutableLiveData<SaveResult>()
    val saveResult: LiveData<SaveResult> = _saveResult

    fun saveItem(productName: String, warehouseName: String?, storageUnitName: String?) {
        val trimmedName = productName.trim()

        // Basic input validation: none of the three fields may be empty.
        if (trimmedName.isEmpty() || warehouseName.isNullOrBlank() || storageUnitName.isNullOrBlank()) {
            _saveResult.value = SaveResult.ValidationError
            return
        }

        viewModelScope.launch {
            repository.insert(
                InventoryItem(
                    productName = trimmedName,
                    warehouseName = warehouseName,
                    storageUnitName = storageUnitName
                )
            )
            _saveResult.value = SaveResult.Success
        }
    }
}
