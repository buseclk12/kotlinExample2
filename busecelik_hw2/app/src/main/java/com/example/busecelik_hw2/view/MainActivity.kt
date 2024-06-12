package com.example.busecelik_hw2.view
//add favorite kısmını - updated a çevir, sonrasında ayrı bir activity e atıp price değişikliğini yap!!

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.busecelik_hw2.ApiService
import com.example.busecelik_hw2.adapter.CustomRecyclerViewAdapter
import com.example.busecelik_hw2.databinding.ActivityMainBinding
import com.example.busecelik_hw2.db.House
import com.example.busecelik_hw2.db.HouseRoomDatabase
import com.example.busecelik_hw2.util.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), GestureDetector.OnGestureListener {
    private lateinit var retrofit: Retrofit
    private lateinit var apiService: ApiService
    private lateinit var gestureDetector: GestureDetector
    lateinit var binding: ActivityMainBinding
    private val houseDB: HouseRoomDatabase by lazy {
        Room.databaseBuilder(this, HouseRoomDatabase::class.java, Constants.DATABASENAME)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
    private var adapter: CustomRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRetrofit()
        fetchHouses()

        binding.recyclerCustomer.layoutManager = LinearLayoutManager(this)
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                return true
            }
        })

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddHouseActivity::class.java)
            startActivity(intent)
        }
        binding.fabDeleteAll.setOnClickListener {
            houseDB.houseDao().deleteAllHouses()
            displayHouses()
        }
    }

    private fun setupRetrofit() {
        retrofit = Retrofit.Builder()
            .baseUrl("https://www.jsonkeeper.com/b/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)
    }

    private fun fetchHouses() {
        apiService.getHouses().enqueue(object : Callback<List<House>> {
            override fun onResponse(call: Call<List<House>>, response: Response<List<House>>) {
                if (response.isSuccessful) {
                    response.body()?.let { houses ->
                        Log.d("MainActivity", "Fetched houses: $houses")
                        houseDB.houseDao().insertAll(houses)
                        displayHouses()
                    } ?: run {
                        Log.e("MainActivity", "Response body is null")
                    }
                } else {
                    Log.e("MainActivity", "Failed to retrieve data: ${response.errorBody()?.string()}")
                    Toast.makeText(this@MainActivity, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<House>>, t: Throwable) {
                Log.e("MainActivity", "Error: ${t.message}", t)
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayHouses() {
        val houses = houseDB.houseDao().getAllHouses()
        adapter = CustomRecyclerViewAdapter(this, houses)
        binding.recyclerCustomer.adapter = adapter
        adapter?.setOnItemLongPressListener(object : CustomRecyclerViewAdapter.OnItemLongPressListener {
            override fun onItemLongPressed(house: House) {
                Toast.makeText(this@MainActivity, "Favorited", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }

    override fun onResume() {
        super.onResume()
        displayHouses()  // Refresh the list of houses
    }

    override fun onDown(e: MotionEvent): Boolean {
        // Not implemented
        return false
    }

    override fun onShowPress(e: MotionEvent) {
        // Not implemented
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        // Not implemented
        return false
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        // Not implemented
        return false
    }

    override fun onLongPress(e: MotionEvent) {
        // Not implemented
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        // Not implemented
        return false
    }
}
