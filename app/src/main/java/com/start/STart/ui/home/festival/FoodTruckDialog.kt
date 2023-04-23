package com.start.STart.ui.home.festival

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.skydoves.cloudy.Cloudy
import com.start.STart.api.ApiClient
import com.start.STart.api.festival.response.FoodTruckModel
import com.start.STart.databinding.DialogFoodTruckBinding
import com.start.STart.ui.home.festival.info.foodtruck.FoodTruck2Adapter
import com.start.STart.ui.home.festival.info.foodtruck.FoodTruckAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodTruckDialog: DialogFragment() {
    private var _binding: DialogFoodTruckBinding? = null
    private val binding get() = _binding!!
    private val foodTruckAdapter by lazy { FoodTruckAdapter() }
    private val foodTruck2Adapter by lazy { FoodTruck2Adapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.composeView.setContent {
            Cloudy(radius = 10, allowAccumulate = { true }){

            }
        }

        binding.foodLake.adapter = foodTruckAdapter
        binding.foodGround.adapter = foodTruck2Adapter
        loadFoodTruck()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFoodTruckBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun loadFoodTruck() {
        ApiClient.festivalService.loadFoodTruck()
            .enqueue(object : Callback<FoodTruckModel> {
                override fun onResponse(call: Call<FoodTruckModel>, response: Response<FoodTruckModel>) {
                    if(response.isSuccessful) {

                        foodTruckAdapter.addItem(response.body()!!.data[0].truckList)
                        foodTruck2Adapter.addItem(response.body()!!.data[0].truckList)

                        foodTruckAdapter.notifyDataSetChanged()
                        foodTruck2Adapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<FoodTruckModel>, t: Throwable) {
                    Log.d("tag", t.message.toString())
                }
            })
    }
}