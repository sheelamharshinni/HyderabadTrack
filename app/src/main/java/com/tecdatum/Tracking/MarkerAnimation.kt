import com.google.android.gms.maps.model.LatLng
import java.util.ArrayList

//package com.tecdatum.Tracking
//
//import android.os.Handler
//import android.os.SystemClock
//import android.view.animation.AccelerateDecelerateInterpolator
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.model.*
//import com.tecdatum.Tracking.School.LatLngInterpolator
//import com.tecdatum.Tracking.School.MapsActivity
//import com.tecdatum.Tracking.School.R
//
//object MarkerAnimation {
//    var isMarkerRotating = false
//    internal var lat = 0.0
//    internal var lng = 0.04
//    fun animateMarkerToGB(marker: Marker, finalPosition: LatLng, latLngInterpolator: LatLngInterpolator, googleMap: GoogleMap) {
//        // if (marker != null) {
//        // marker.remove();
//        // }
//        if (!isMarkerRotating) {
//            val startPosition = marker.getPosition()
//            val handler = Handler()
//            val start = SystemClock.uptimeMillis()
//            val interpolator = AccelerateDecelerateInterpolator()
//            val durationInMs = 2000f
//            handler.post(object : Runnable {
//                internal var elapsed: Long = 0
//                internal var t: Float = 0.toFloat()
//                internal var v: Float = 0.toFloat()
//                public override fun run() {
//                    isMarkerRotating = true
//                    // Ca lculate progress using interpolator
//                    elapsed = SystemClock.uptimeMillis() - start
//                    t = elapsed / durationInMs
//                    v = interpolator.getInterpolation(t)
//                    marker.setPosition(latLngInterpolator.interpolate(v, startPosition, finalPosition))
//                    marker.setRotation(MarkerAnimation.getBearing(startPosition, finalPosition))
//                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.Builder().target(finalPosition).zoom(16f).build()))
//                    // Repeat till progress is complete.
//                    if (t < 1) {
//                        // Post again 10ms later.
//                        handler.postDelayed(this, 10)
//                    } else {
//                        isMarkerRotating = false
//                    }
//                }
//            })
//        }
//    }
//
//    fun getBearing(begin: LatLng, end: LatLng): Float {
//        lat = Math.abs(begin.latitude - end.latitude)
//        lng = Math.abs(begin.longitude - end.longitude)
//        // Log.e("LATLANGVALUES","END"+end);
//        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
//            return (Math.toDegrees(Math.atan(lng / lat))).toFloat()
//        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
//            return ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90).toFloat()
//        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
//            return (Math.toDegrees(Math.atan(lng / lat)) + 180).toFloat()
//        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
//            return ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270).toFloat()
//        return -1f
//    }
//}

