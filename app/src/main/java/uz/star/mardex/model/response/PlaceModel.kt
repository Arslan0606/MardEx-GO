package uz.star.mardex.model.response

import java.io.Serializable

data class PlaceModel(
    var title: String,
    var subtitle: String,
    var distance: String,
    var allReview: Int?,
    var score: Float?,
    var longitude: Double,
    var latitude: Double
) : Serializable