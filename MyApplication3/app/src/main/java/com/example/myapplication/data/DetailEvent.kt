package com.example.myapplication.data
import java.io.Serializable
data class DetailEvent(
    var seq: Int?,
    var title: String?,
    var imgUrl:String?,
    var gpsX:Double?,
    var gpsY:Double?,
    var startDate: Int?,
    var endDate: Int?,
    var place:String?,
    var price: String?,
): Serializable