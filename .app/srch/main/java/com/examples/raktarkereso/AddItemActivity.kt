package com.example.raktarkereso

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.raktarkereso.data.AppDatabase
import com.example.raktarkereso.data.InventoryRepository
import com.example.raktarkereso.data.WarehouseConstants
import com.example.raktarkereso.databinding.ActivityAddItemBinding
import com.example.raktarkereso.viewmodel.AddItemViewModel
import com.example.raktarkereso.viewmodel.SaveResult
import com.example.raktarkereso.viewmodel.ViewModelFactory

/**
 * "Add new product" screen: warehouse dropdown, storage unit dropdown,
 * product name field, and a Save button with basic validation.
 */
class AddItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddItemBinding
    private lateinit var viewModel: AddItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.add_item_title)

        val dao = AppDatabase.getDatabase(applicationContext).inventoryDao()
        val repository = InventoryRepository(dao)
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[AddItemViewModel::class.java]

        setupDropdowns()
        setupSaveButton()
        observeViewModel()
    }

    private fun setupDropdowns() {
        val warehouseAdapter = ArrayAdapter(
            this, android.R.layout.simple_dropdown_item_1line, WarehouseConstants.WAREHOUSES
        )
        binding.dropdownWarehouse.setAdapter(warehouseAdapter)

        val storageUnitAdapter = ArrayAdapter(
            this, android.R.layout.simple_dropdown_item_1line, WarehouseConstants.STORAGE_UNITS
        )
        binding.dropdownStorageUnit.setAdapter(storageUnitAdapter)
    }

    private fun setupSaveButton() {
        binding.buttonSave.setOnClickListener {
            val productName = binding.editProductName.text?.toString().orEmpty()
            val warehouse = binding.dropdownWarehouse.text?.toString()
            val storageUnit = binding.dropdownStorageUnit.text?.toString()

            viewModel.saveItem(productName, warehouse, storageUnit)
        }
    }

    private fun observeViewModel() {
        viewModel.saveResult.observe(this) { result ->
            when (result) {
                is SaveResult.Success -> {
                    Toast.makeText(this, R.string.item_saved_success, Toast.LENGTH_SHORT).show()
                    finish()
                }
                is SaveResult.ValidationError -> {
                    Toast.makeText(this, R.string.validation_error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
