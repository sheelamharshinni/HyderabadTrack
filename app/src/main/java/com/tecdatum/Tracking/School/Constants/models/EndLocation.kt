package com.tecdatum.Tracking.School.models
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EndLocation {
    @SerializedName("lat")
    @Expose
    var lat: Double? = null
    @SerializedName("lng")
    @Expose
    var lng: Double? = null

}