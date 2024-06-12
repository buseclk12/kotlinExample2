package com.example.busecelik_hw2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.busecelik_hw2.R
import com.example.busecelik_hw2.db.House

class EditHouseAdapter(
    private val house: House,
    private val onSaveClick: (Double) -> Unit
) : RecyclerView.Adapter<EditHouseAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_edit_house, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(house)
    }

    override fun getItemCount(): Int = 1

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvHouseName: TextView = itemView.findViewById(R.id.tvHouseName)
        private val etHousePrice: EditText = itemView.findViewById(R.id.etHousePrice)

        fun bind(house: House) {
            tvHouseName.text = house.name
            etHousePrice.setText(house.price.toString())

            itemView.findViewById<View>(R.id.btnSaveHouse).setOnClickListener {
                val updatedPrice = etHousePrice.text.toString().toDoubleOrNull()
                if (updatedPrice != null) {
                    onSaveClick(updatedPrice)
                } else {
                    etHousePrice.error = "Invalid price"
                }
            }
        }
    }
}
