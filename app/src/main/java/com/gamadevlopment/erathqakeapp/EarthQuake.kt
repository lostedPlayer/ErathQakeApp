package com.gamadevlopment.erathqakeapp

data class EarthQuake(
    var mag: Double,
    var place: String,
    var time: Long,
    var longitude: Double,
    var latitude: Double,
    var depth: Double
) {
}