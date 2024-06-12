package com.example.busecelik_hw2.view

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.busecelik_hw2.R
import com.example.busecelik_hw2.databinding.ActivityAddHouseBinding
import com.example.busecelik_hw2.db.House
import com.example.busecelik_hw2.db.HouseRoomDatabase
import com.google.android.material.snackbar.Snackbar

class AddHouseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddHouseBinding
    private lateinit var houseDB: HouseRoomDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        binding = ActivityAddHouseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        houseDB = Room.databaseBuilder(
            applicationContext,
            HouseRoomDatabase::class.java, "houseDB"
        ).allowMainThreadQueries()  // This is not recommended for production code
            .fallbackToDestructiveMigration() // Handles database version upgrades
            .build()
        setupSpinner()

        binding.btnAddHouse.setOnClickListener {
            addHouseToDatabase()
        }
    }

    private fun setupSpinner() {
        val houseTypes = arrayOf("one_to_one", "two_to_one", "three_to_one")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, houseTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerHouseType.adapter = adapter

        // Setting a listener to change the image based on the selected item
        binding.spinnerHouseType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedType = houseTypes[position]
                val imageResId = when (selectedType) {
                    "one_to_one" -> R.drawable.one_to_one
                    "two_to_one" -> R.drawable.two_to_one
                    "three_to_one" -> R.drawable.three_to_one
                    else -> 0
                }
                binding.imageViewHouseType.setImageResource(imageResId)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Optionally clear the image or set it to a default
                binding.imageViewHouseType.setImageResource(0)
            }
        }
    }
    private fun addHouseToDatabase() {
        try {
            val name = binding.editTextHouseName.text.toString()
            val price = binding.editTextHousePrice.text.toString().toDouble() // This can throw a NumberFormatException if not a valid double
            val description = binding.editTextHouseDescription.text.toString()
            val type = binding.spinnerHouseType.selectedItem.toString()

            val house = House(0, name, price, description, type)
            houseDB.houseDao().insertHouse(house)

            Snackbar.make(binding.root, "House added successfully!", Snackbar.LENGTH_LONG).show()
            finish()  // Close this activity and return to the previous one
        } catch (e: Exception) {
            Snackbar.make(binding.root, "Failed to add house: ${e.message}", Snackbar.LENGTH_LONG).show()
        }
    }

}
