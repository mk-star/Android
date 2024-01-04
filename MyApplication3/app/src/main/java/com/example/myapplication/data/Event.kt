package com.example.myapplication.data
import java.io.Serializable
data class Event(
    var seq: Int?,
    var title: String?,
    var thumbnail:String?,
): Serializable
