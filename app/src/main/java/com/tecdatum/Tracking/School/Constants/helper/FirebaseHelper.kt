package com.tecdatum.Tracking.School.Constants.helper

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tecdatum.Tracking.School.Constants.model.Driver

class FirebaseHelper constructor(driverId: String) {

    companion object {
        private const val ONLINE_DRIVERS = "online_drivers"
    }

    private val onlineDriverDatabaseReference: DatabaseReference = FirebaseDatabase
            .getInstance()
            .reference
            .child(ONLINE_DRIVERS)
            .child(driverId)

    init {
        onlineDriverDatabaseReference
                .onDisconnect()
                .removeValue()


    }

    fun updateDriver(driver: Driver) {


        onlineDriverDatabaseReference
                .setValue(driver)
        Log.e("Driver Info", " Updated")
    }

}