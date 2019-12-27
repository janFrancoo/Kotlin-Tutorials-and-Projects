package com.janfranco.kotlintravelbook

class Place {

    var placeId : Int? = null
    var placeName : String? = null
    var latitude : Double? = null
    var longitude : Double? = null

    constructor(placeId : Int, placeName : String, latitude : Double, longitude : Double) {
        this.placeId = placeId
        this.placeName = placeName
        this.latitude = latitude
        this.longitude = longitude
    }

    override fun toString(): String {
        return this.placeName!!
    }

}
