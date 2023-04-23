package com.start.STart.ui.home.festival.info.foodtruck

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.start.STart.api.festival.response.FoodTruck
import com.start.STart.databinding.ItemFoodtruckBinding

class FoodTruckAdapter: RecyclerView.Adapter<FoodTruckAdapter.FoodTruckViewHolder>() {
    var list: MutableList<FoodTruck> = mutableListOf()

    fun addItem(foodTruck: List<FoodTruck>) {
        for(i in foodTruck.indices) {
            if(foodTruck[i].truckLocation == "붕어방") {
                list.add(foodTruck[i])
            }
        }

    }

    inner class FoodTruckViewHolder(val binding: ItemFoodtruckBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(foodTruck: FoodTruck) {
            binding.foodTruckName.text = foodTruck.truckName
            binding.foodTruckDescription.text = foodTruck.truckDescription
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodTruckViewHolder {
        val binding = ItemFoodtruckBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodTruckViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: FoodTruckViewHolder, position: Int) {
        holder.bind(list[position])
    }
}
