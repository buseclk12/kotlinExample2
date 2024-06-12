package com.example.busecelik_hw2.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [House::class], version = 1)
abstract class HouseRoomDatabase : RoomDatabase() {
    abstract fun houseDao(): HouseDao
}
