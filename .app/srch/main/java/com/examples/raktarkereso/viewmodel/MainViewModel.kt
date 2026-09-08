package com.example.raktarkereso.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.raktarkereso.data.InventoryItem
import com.example.raktarkereso.data.InventoryRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for the main search screen.
 * Holds the current search results and whether a search has actually
 * been performed yet, so the UI knows when to show "Nincs találat".
 */
class MainViewModel(private val repository: InventoryRepository) : ViewModel() {

    private val _searchResults = MutableLiveData<List<InventoryItem>>(emptyList())
    val searchResults: LiveData<List<InventoryItem>> = _searchResults

    private val _hasSearched = MutableLiveData(false)
    val hasSearched: LiveData<Boolean> = _hasSearched

    fun search(query: String) {
        val trimmed = query.trim()

        // Empty query: clear results, don't show the "no results" message.
        if (trimmed.isEmpty()) {
            _searchResults.value = emptyList()
            _hasSearched.value = false
            return
        }

        viewModelScope.launch {
            val results = repository.search(trimmed)
            _searchResults.value = results
            _hasSearched.value = true
        }
    }
}
