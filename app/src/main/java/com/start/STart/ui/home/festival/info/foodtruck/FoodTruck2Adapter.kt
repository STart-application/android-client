package com.start.STart.ui.home.festival.info.foodtruck

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.start.STart.api.festival.response.FoodTruck
import com.start.STart.databinding.ItemFoodtruckBinding
import com.start.STart.ui.home.festival.info.foodtruck.FoodTruck2Adapter.FoodTruck2ViewHolder


class FoodTruck2Adapter: RecyclerView.Adapter<FoodTruck2ViewHolder>() {
    var list: MutableList<FoodTruck> = mutableListOf()

    fun addItem(foodTruck: List<FoodTruck>) {
        for(i in foodTruck.indices) {
            if(foodTruck[i].truckLocation == "운동장") {
                list.add(foodTruck[i])
            }
        }

    }

    inner class FoodTruck2ViewHolder(val binding: ItemFoodtruckBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(foodTruck: FoodTruck) {
            binding.foodTruckName.text = foodTruck.truckName
            binding.foodTruckDescription.text = foodTruck.truckDescription
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodTruck2ViewHolder {
        val binding = ItemFoodtruckBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodTruck2ViewHolder(
            binding
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: FoodTruck2ViewHolder, position: Int) {
        holder.bind(list[position])
    }
}
