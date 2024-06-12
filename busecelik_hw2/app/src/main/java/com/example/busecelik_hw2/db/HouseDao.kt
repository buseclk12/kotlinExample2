package com.example.busecelik_hw2.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.busecelik_hw2.util.Constants


@Dao
interface HouseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHouse(house: House)

    @Update
    fun updateHouse(house: House)

    @Delete
    fun deleteHouse(house: House)

    @Query("DELETE FROM ${Constants.TABLENAME}")
    fun deleteAllHouses()

    @Query("SELECT * FROM ${Constants.TABLENAME} ORDER BY id DESC")
    fun getAllHouses(): MutableList<House>

    @Query("SELECT * FROM ${Constants.TABLENAME} WHERE id = :houseId")
    fun getHouseById(houseId: Int): House?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(houses: List<House>) {
        houses.forEach {
            insertHouse(it)
        }
    }
}
