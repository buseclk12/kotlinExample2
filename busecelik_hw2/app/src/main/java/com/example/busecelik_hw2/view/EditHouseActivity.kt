package com.example.busecelik_hw2.view

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.busecelik_hw2.R
import com.example.busecelik_hw2.adapter.EditHouseAdapter
import com.example.busecelik_hw2.databinding.ActivityEditHouseBinding
import com.example.busecelik_hw2.db.House
import com.example.busecelik_hw2.db.HouseRoomDatabase
import com.example.busecelik_hw2.util.Constants

class EditHouseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditHouseBinding
    private lateinit var houseDB: HouseRoomDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding = ActivityEditHouseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        houseDB = Room.databaseBuilder(this, HouseRoomDatabase::class.java, "houseDB")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

        val houseId = intent.getIntExtra("houseId", -1)
        val house = houseDB.houseDao().getHouseById(houseId)

        if (house != null) {
            binding.editTextHouseName.setText(house.name)
            binding.editTextHousePrice.setText(house.price.toString())
            binding.editTextHouseDescription.setText(house.description)
        }

        binding.btnSaveHouse.setOnClickListener {
            val updatedName = binding.editTextHouseName.text.toString()
            val updatedPrice = binding.editTextHousePrice.text.toString().toDouble()
            val updatedDescription = binding.editTextHouseDescription.text.toString()

            house?.name = updatedName
            house?.price = updatedPrice
            house?.description = updatedDescription

            houseDB.houseDao().updateHouse(house!!)
            Toast.makeText(this, "House updated successfully", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}