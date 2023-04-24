package com.start.STart.ui.home.festival.dialogs

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.skydoves.cloudy.Cloudy
import com.start.STart.api.ApiClient
import com.start.STart.api.festival.response.FoodTruck
import com.start.STart.api.festival.response.FoodTruckModel
import com.start.STart.databinding.DialogFoodTruckBinding
import com.start.STart.databinding.ItemFoodtruckBinding
import com.start.STart.util.contains
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodTruckDialog: DialogFragment() {
    private var _binding: DialogFoodTruckBinding? = null
    private val binding get() = _binding!!
    //private val foodTruckAdapter by lazy { FoodTruckAdapter() }
    //private val foodTruck2Adapter by lazy { FoodTruck2Adapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        initBackground()
    }

    override fun onStart() {
        super.onStart()
        loadFoodTruck()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initBackground() {
        binding.composeView.setContent { Cloudy(radius = 10, allowAccumulate = { true }){} }
        binding.dim.setOnTouchListener { view, motionEvent ->
            if(!binding.cardView.contains(motionEvent.rawX.toInt(), motionEvent.rawY.toInt())) {
                dismiss()
            }
            true
        }
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
                        val map = response.body()!!.data[0].truckList.groupBy { it.truckLocation }

                        map["붕어방"]?.forEach {
                            addItem(it, binding.foodLake)
                        }

                        map["운동장"]?.forEach {
                            addItem(it, binding.foodGround)
                        }

                    }
                }

                override fun onFailure(call: Call<FoodTruckModel>, t: Throwable) {
                    Log.d("tag", t.message.toString())
                }
            })
    }

    private fun addItem(foodTruck: FoodTruck, layout: LinearLayout) {
        val itemBinding = ItemFoodtruckBinding.inflate(LayoutInflater.from(requireContext()), null, false)
        itemBinding.foodTruckName.text = foodTruck.truckName
        itemBinding.foodTruckDescription.text = foodTruck.truckDescription
        layout.addView(itemBinding.root)
    }
}