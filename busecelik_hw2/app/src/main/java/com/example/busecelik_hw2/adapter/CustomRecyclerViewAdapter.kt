package com.example.busecelik_hw2.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.busecelik_hw2.R
import com.example.busecelik_hw2.db.House
import com.example.busecelik_hw2.view.EditHouseActivity

class CustomRecyclerViewAdapter(
    private val context: Context,
    private val houses: MutableList<House>,
) : RecyclerView.Adapter<CustomRecyclerViewAdapter.RecyclerViewItemHolder>() {

    private var longPressListener: OnItemLongPressListener? = null

    interface OnItemLongPressListener {
        fun onItemLongPressed(house: House)
    }

    fun setOnItemLongPressListener(listener: OnItemLongPressListener) {
        longPressListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_layout, parent, false)
        return RecyclerViewItemHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewItemHolder, position: Int) {
        val house = houses[position]
        holder.tvItemHouseName.text = house.name
        holder.tvItemHousePrice.text = context.getString(R.string.price_format, house.price)
        holder.tvItemHouseDescription.text = house.description
        val imageResId = context.resources.getIdentifier(
            house.type.substringBeforeLast('.'), "drawable", context.packageName
        )
        if (imageResId == 0) {
            Log.e("LoadImage", "Resource not found for house type: ${house.type}")
            holder.ivHouseType.setImageResource(R.drawable.three_to_one)  // Set default image
        } else {
            holder.ivHouseType.setImageResource(imageResId)
        }

        holder.itemView.setOnClickListener {
            showDetailDialog(house)
        }

        holder.itemView.setOnLongClickListener {
            longPressListener?.onItemLongPressed(houses[holder.adapterPosition])
            true
        }
    }

    override fun getItemCount(): Int = houses.size

    private fun showDetailDialog(house: House) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_house_details, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(dialogView)

        val tvName = dialogView.findViewById<TextView>(R.id.tvDialogHouseName)
        val tvPrice = dialogView.findViewById<TextView>(R.id.tvDialogHousePrice)
        val tvDescription = dialogView.findViewById<TextView>(R.id.tvDialogHouseDescription)
        val ivType = dialogView.findViewById<ImageView>(R.id.ivDialogHouseType)
        val btnClose = dialogView.findViewById<Button>(R.id.btnClose)
        val btnUpdate = dialogView.findViewById<Button>(R.id.btnUpdate)

        tvName.text = house.name
        tvPrice.text = context.getString(R.string.price_format, house.price)
        tvDescription.text = house.description
        ivType.setImageResource(context.resources.getIdentifier(house.type, "drawable", context.packageName))

        val dialog = builder.create()

        btnClose.setOnClickListener { dialog.dismiss() }
        btnUpdate.setOnClickListener {
            val intent = Intent(context, EditHouseActivity::class.java)
            intent.putExtra("houseId", house.id)  // Pass house ID or the entire House object if Parcelable
            context.startActivity(intent)
            dialog.dismiss()
        }

        dialog.show()
    }
    inner class RecyclerViewItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvItemHouseName: TextView = itemView.findViewById(R.id.tvItemHouseName)
        var tvItemHousePrice: TextView = itemView.findViewById(R.id.tvItemHousePrice)
        var tvItemHouseDescription: TextView = itemView.findViewById(R.id.tvItemHouseDescription)
        var ivHouseType: ImageView = itemView.findViewById(R.id.ivHouseType)
    }
}
