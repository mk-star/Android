package com.example.myapplication.data

import java.io.Serializable

data class Review(
    var reviewSeq: Int?,
    var reviewText: String?,
    var reviewTitle: String?,
    var reviewImage: String?,
) : Serializable
