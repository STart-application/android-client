package com.start.STart.api.festival.response

data class FestivalInfoData(
    val boothList: List<BoothData>,
    val lineUpList: List<LineUpData>,
)

data class FoodTruckModel(
    val status: Int,
    val message: String,
    val data: List<TruckDataList>
)

data class TruckDataList(
    val truckList: List<FoodTruck>
)

data class FoodTruck(
    val truckId: Int,
    val truckName: String,
    val truckDescription: String,
    val truckImageUrl: String,
    val truckLocation: String
)

data class PhotoZoneModel(
    val status: Int,
    val message: String,
    val data: List<PhotoDataList>
)
data class PhotoDataList(
    val photoList: List<PhotoZone>
)
data class PhotoZone(
    val photoId: Int,
    val photoName: String,
    val photoDescription: String,
    val photoImageUrl: String
)