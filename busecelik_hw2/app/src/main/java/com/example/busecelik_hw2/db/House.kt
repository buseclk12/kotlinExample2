package com.example.busecelik_hw2.db
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.busecelik_hw2.util.Constants

@Entity(tableName = Constants.TABLENAME)
class House(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String,
    var price: Double,
    var description: String,
    var type: String // This will store the image filename like 'onetoone.png'
) {
    override fun toString(): String {
        return "House(id=$id, name='$name', price=$price, description='$description', type='$type')"
    }
}
