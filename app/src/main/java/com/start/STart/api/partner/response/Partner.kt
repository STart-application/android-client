package com.start.STart.api.partner.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class PartnerList(
    val partnerList: List<Partner>,
)

@Parcelize
data class Partner(
    val id: Int,
    val address: String?,
    val benefit: String?,
    val coordinateX: Double?,
    val coordinateY: Double?,
    val description: String?,
    val imageUrl: String?,
    val name: String,
    val partnerTypeId: Int,
    val phoneNumber: String?
): Parcelable