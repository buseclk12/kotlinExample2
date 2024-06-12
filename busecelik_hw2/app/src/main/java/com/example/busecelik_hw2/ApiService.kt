package com.example.busecelik_hw2

import com.example.busecelik_hw2.db.House
import retrofit2.http.GET
import retrofit2.Call


interface ApiService {
    @GET("87ZW")
    fun getHouses(): Call<List<House>>
}
