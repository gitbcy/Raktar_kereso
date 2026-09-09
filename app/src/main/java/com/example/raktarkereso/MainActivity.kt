package com.example.raktarkereso

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.raktarkereso.adapter.SearchResultAdapter
import com.example.raktarkereso.data.AppDatabase
import com.example.raktarkereso.data.InventoryRepository
import com.example.raktarkereso.databinding.ActivityMainBinding
import com.example.raktarkereso.viewmodel.MainViewModel
import com.example.raktarkereso.viewmodel.ViewModelFactory

/**
 * Main screen: search bar at the top, live results list below,
 * and a button to open the "add new product" screen.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private val adapter = SearchResultAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Manual dependency setup: DB -> Repository -> ViewModel.
        val dao = AppDatabase.getDatabase(applicationContext).inventoryDao()
        val repository = InventoryRepository(dao)
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        setupRecyclerView()
        setupSearch()
        setupAddButton()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.recyclerResults.layoutManager = LinearLayoutManager(this)
        binding.recyclerResults.adapter = adapter
    }

    private fun setupSearch() {
        // Live search: fire a query on every keystroke.
        binding.editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.search(s?.toString().orEmpty())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupAddButton() {
        binding.buttonAddItem.setOnClickListener {
            startActivity(Intent(this, AddItemActivity::class.java))
        }
    }

    private fun observeViewModel() {
        viewModel.searchResults.observe(this) { results ->
            adapter.submitList(results)
            binding.recyclerResults.visibility = if (results.isEmpty()) View.GONE else View.VISIBLE
        }

        viewModel.hasSearched.observe(this) { hasSearched ->
            val currentResults = viewModel.searchResults.value.orEmpty()
            val showNoResults = hasSearched && currentResults.isEmpty()
            binding.textNoResults.visibility = if (showNoResults) View.VISIBLE else View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        // Re-run the current search when returning from the add-item screen,
        // so a newly added product shows up immediately if it matches.
        val currentQuery = binding.editSearch.text?.toString().orEmpty()
        if (currentQuery.isNotEmpty()) {
            viewModel.search(currentQuery)
        }
    }
}
