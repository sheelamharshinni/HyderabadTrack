package com.tecdatum.Tracking.School.newactivities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.graphics.Color
import android.icu.text.DateFormat
import android.location.Criteria
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.tecdatum.Tracking.School.DateTime.CustomDateTimePicker
import com.tecdatum.Tracking.School.R
import com.tecdatum.Tracking.School.newConstants.Url_new
import com.tecdatum.Tracking.School.newConstants.Url_new.PointsMaster
import com.tecdatum.Tracking.School.newactivities.SplashScreen.MY_PREFS_NAME
import com.tecdatum.Tracking.School.newhelpers.Samplemyclass
import com.tecdatum.Tracking.School.volley.VolleySingleton
import kotlinx.android.synthetic.main.activity_historytracking.*

import kotlinx.android.synthetic.main.custommapview_vechilepoints.view.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PArentHistoryTrackingDetails : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        var markerPlaces = ArrayList<String>()
    }

    var ParentMobile: String? = null
    var UserName: String? = null
    var DisplayName: String? = null
    var StudentID: String? = null
    var StudentClass: String? = null
    var ParentName: String? = null
    private val HistoryTracking = Url_new.HistoryTracking
    var VehicleName: String? = null
    var speed: String? = null
    var lat: String? = null
    var lng: String? = null
    var loc: kotlin.String? = null
    var statuscolor: kotlin.String? = null
    var direction: kotlin.String? = null
    var S_sincefrom: kotlin.String? = null
    var ignition: kotlin.String? = null
    var liveedate: kotlin.String? = null
    var vehicletype: kotlin.String? = null
    var DriverName: kotlin.String? = null
    var MobNo: kotlin.String? = null
    var VehicleId: kotlin.String? = null
    var supportMapFragment: SupportMapFragment? = null
    var VechileId: String? = null
    var pDialog: ProgressDialog? = null
    var googleMap: GoogleMap? = null
    var PointName: String? = null
    var currentLocationMarker_Source: Marker? = null
    var CCtvMarkers: ArrayList<Marker> = ArrayList()
    private val url1 = Url_new.LiveTracking
    var a_VehicleNo: String? = null
    var line: Polyline? = null;

    var a_StartDateTime: kotlin.String? = null
    var a_StartLocation: kotlin.String? = null
    var a_EndDateTime: kotlin.String? = null
    var a_EndLocation: kotlin.String? = null
    var a_Distance: kotlin.String? = null
    var a_TravelTime: kotlin.String? = null
    var a_IdleTime: kotlin.String? = null
    var a_StopTime: kotlin.String? = null
    var message: kotlin.String? = null
    var vehicle: String? = null
    var default_From_date: kotlin.String? = null
    var default_To_date: kotlin.String? = null
    var Lo: kotlin.String? = null
    var DT: kotlin.String? = null
    var Dire: kotlin.String? = null
    var Ign: kotlin.String? = null
    var vehicleT: kotlin.String? = null
    var Current_Address: kotlin.String? = null
    var S_PassWord: kotlin.String? = null
    var IMEI: kotlin.String? = null
    var myDataFromActivity: kotlin.String? = null
    var Start_time: kotlin.String? = null
    var Stop_time: kotlin.String? = null
    var default_mode: kotlin.String? = null
    var MarkerPoints = ArrayList<LatLng>()
    var latt = 0.0
    var lngg = 0.0
    var position: LatLng? = null
    var Locatn_time = ArrayList<String>()
    var Points = ArrayList<LatLng>()
    var custom: CustomDateTimePicker? = null
    var custom1: CustomDateTimePicker? = null
    var date_fm: Calendar? = null
    var date_to: Calendar? = null
    var RouteID: String? = null
    var C_dialog: Dialog? = null
    var polyOptions: PolylineOptions? = null
    var adapter1: ArrayAdapter<Samplemyclass>? = null
    var cl0: ArrayList<*>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historytracking)
        supportMapFragment = supportFragmentManager.findFragmentById(R.id.supportMap) as SupportMapFragment?
        supportMapFragment!!.getMapAsync(this)
        val bb: SharedPreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE)
        ParentMobile = bb.getString("ParentMobile", "")
        UserName = bb.getString("UserName", "")
        StudentID = bb.getString("StudentID", "")
        DisplayName = bb.getString("DisplayName", "")
        ParentName = bb.getString("ParentName", "")
        StudentClass = bb.getString("StudentClass", "")

        RouteID = bb.getString("RouteID", "")
        lay_back.setOnClickListener {
            finish()
        }
        VechileId = bb.getString("VehicleID", "")
        HistoryTracking(VechileId!!)

        select.setOnClickListener {
            fromdata()
        }
        val c = Calendar.getInstance()
        val timeOfDay = c.get(Calendar.HOUR_OF_DAY)

        if (cb_points.isChecked == true) {

            if (timeOfDay < 11) {

                RouteID?.let { getpickpoints("PickPoints", it) }

            } else if (timeOfDay >= 11) {

                RouteID?.let { getpickpoints("DropPoints", it) }


            }

        } else {
            hideCCtvs()
        }
        cb_points.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            val c = Calendar.getInstance()
            val timeOfDay = c.get(Calendar.HOUR_OF_DAY)

            if (b) {

                if (timeOfDay < 11) {

                    RouteID?.let { getpickpoints("PickPoints", it) }

                } else if (timeOfDay >= 11) {

                    RouteID?.let { getpickpoints("DropPoints", it) }


                }

            } else {
                hideCCtvs()
            }
        })

    }

    private fun getpickpoints(sp_pickup_Name: String, pointId: String) {
        var sp_pickup_Name: String? = sp_pickup_Name
        var pointId: String? = pointId
        val jsonBody = JSONObject()
        if (sp_pickup_Name == null) {
            sp_pickup_Name = ""
        }
        if (pointId == null) {
            pointId = ""
        }
        try {
            jsonBody.put("ActionName", "" + sp_pickup_Name)
            jsonBody.put("RouteID", "" + pointId)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val mRequestBody = jsonBody.toString()
        Log.e("VOLLEY", "PointsMaster$mRequestBody")
        val stringRequest: StringRequest = object : StringRequest(Method.POST, PointsMaster, com.android.volley.Response.Listener { response ->
            Log.e("VOLLEY", "PointsMaster$response")
            try {
                val `object` = JSONObject(response)
                val code = `object`.optString("Code")

                val message = `object`.optString("Message")
                if (code.equals("0", ignoreCase = true)) {
                    val jArray = `object`.getJSONArray("PointData")
                    val number = jArray.length()
                    val num = Integer.toString(number)
                    if (number == 0) {
                        Toast.makeText(this@PArentHistoryTrackingDetails, " No Data Found", Toast.LENGTH_LONG).show()
                    } else {
                        for (i in 0 until number) {
                            val json_data = jArray.getJSONObject(i)
                            val PointID = json_data.getString("PointID")
                            val TypeOfPointsID = json_data.getString("TypeOfPointsID")
                            val PointType = json_data.getString("PointType")
                            val PointCode = json_data.getString("PointCode")

                            PointName = json_data.getString("PointName")
                            val Location = json_data.getString("Location")
                            val Latitude = json_data.getString("Latitude")
                            val Longitude = json_data.getString("Longitude")

                            val Title: String = "" + "_" + PointName
                            currentLocationMarker_Source = googleMap!!.addMarker(MarkerOptions()
                                    .position(LatLng(Latitude.toDouble(), Longitude.toDouble())).title(Title)
                                    .snippet(VehicleName)
                                    .draggable(false)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_allpoints)))


                            val customInfoWindow = CustomInfoWindow(layoutInflater)
                            googleMap!!.setInfoWindowAdapter(customInfoWindow)
                            CCtvMarkers.add(currentLocationMarker_Source!!)
                            markerPlaces.add(currentLocationMarker_Source!!.getId())
                        }


                    }

                } else {

                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, com.android.volley.Response.ErrorListener { error -> Log.e("VOLLEY", error.toString()) }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                return mRequestBody?.toByteArray(StandardCharsets.UTF_8)
            }
        }
        VolleySingleton.getInstance().requestQueue.add(stringRequest)
    }

    private fun hideCCtvs() {
        for (marker in CCtvMarkers) {
            marker.remove()
        }
    }

    private fun HistoryTracking1(vehicleid: String, fromdate: String, todate: String) {
        pDialog = ProgressDialog(this@PArentHistoryTrackingDetails)
        pDialog!!.setMessage("Please Wait While Redirect..")
        pDialog!!.setIndeterminate(false)
        pDialog!!.setCancelable(true)
        pDialog!!.show()
        val bb: SharedPreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE)
        VechileId = bb.getString("VehicleID", "")

        try {
            val jsonBody = JSONObject()

            jsonBody.put("Mode", default_mode)
            jsonBody.put("VehicleId", vehicleid)
            jsonBody.put("StartDate", fromdate)
            jsonBody.put("EndDate", todate)
            jsonBody.put("Duration", "5")
            val mRequestBody = jsonBody.toString()
            Log.e("HistoryTracking", "request HistoryTracking$mRequestBody")
            val stringRequest: StringRequest = object : StringRequest(Method.POST, HistoryTracking, Response.Listener { response ->
                Log.i("HistoryTracking", "Response HistoryTracking$response")
                try {
                    val `object` = JSONObject(response)
                    val code = `object`.optString("Code").toString()
                    if (code.equals("0", ignoreCase = true)) { //  Track_info.setVisibility(View.VISIBLE);
                        val jArray = `object`.getJSONArray("HistoryData")
                        val number = jArray.length()
                        val num = Integer.toString(number)
                        Log.i("history lat", num)
                        if (number == 0) {
                        } else {

                            googleMap!!.clear()
                            if (Points != null) {
                                Points.clear()
                            }

                            for (i in 0 until number) {
                                val json_data = jArray.getJSONObject(i)
                                a_VehicleNo = json_data.getString("Vehicle").toString()
                                lat = json_data.getString("Latitude").toString()
                                lng = json_data.getString("Longitude").toString()
                                Lo = json_data.getString("Location").toString()
                                DT = json_data.getString("Datetime").toString()
                                Dire = json_data.getString("Speed").toString()
                                Ign = json_data.getString("Ignition").toString()
                                vehicleT = json_data.getString("VehicleType").toString()
                                latt = lat!!.toDouble()
                                lngg = lng!!.toDouble()
                                position = LatLng(latt, lngg)
                                Locatn_time.add("" + DT)
                                Points.add(position!!)
                                googleMap!!.addMarker(MarkerOptions()
                                        .position(position!!).title(vehicle)
                                        .snippet(Lo).icon(BitmapDescriptorFactory.fromResource(R.drawable.points)))
                            }
                            pDialog!!.dismiss()

                            drawPolyLineOnMap(Points)
                            Log.d("history la", Points.toString())
                            googleMap!!.addMarker(MarkerOptions()
                                    .position(Points.get(0)).title("Start")
                                    .snippet(Locatn_time.get(0)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_start)))
                            googleMap!!.addMarker(MarkerOptions()
                                    .position(Points.get(Points.size - 1)).title("End")
                                    .snippet(Locatn_time.get(Points.size - 1)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_stop)))
                        }
                    } else {
                        googleMap!!.clear()
                        pDialog!!.dismiss()

                        var alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@PArentHistoryTrackingDetails)
                        alertDialog.setTitle("Alert")
                        alertDialog.setMessage("" + "No Data Found")
                        alertDialog.setIcon(R.drawable.alert)
                        alertDialog.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
                        alertDialog.show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error ->
                Log.e("VOLLEY", error.toString())
                pDialog!!.dismiss()
            }) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                @RequiresApi(Build.VERSION_CODES.KITKAT)
                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    return mRequestBody?.toByteArray(StandardCharsets.UTF_8)
                } //
            }
            stringRequest.retryPolicy = DefaultRetryPolicy(
                    25000000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            VolleySingleton.getInstance().getRequestQueue().add(stringRequest)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun HistoryTracking(vehicleid: String) {
        pDialog = ProgressDialog(this@PArentHistoryTrackingDetails)
        pDialog!!.setMessage("Please Wait While Redirect..")
        pDialog!!.setIndeterminate(false)
        pDialog!!.setCancelable(true)
        pDialog!!.show()
        val bb: SharedPreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE)
        VechileId = bb.getString("VehicleID", "")
        setdate()
        try {
            val jsonBody = JSONObject()

            jsonBody.put("Mode", default_mode)
            jsonBody.put("VehicleId", vehicleid)
            jsonBody.put("StartDate", default_From_date)
            jsonBody.put("EndDate", default_To_date)
            jsonBody.put("Duration", "5")
            val mRequestBody = jsonBody.toString()
            Log.e("HistoryTracking", "request HistoryTracking$mRequestBody")
            val stringRequest: StringRequest = object : StringRequest(Method.POST, HistoryTracking, Response.Listener { response ->
                Log.i("HistoryTracking", "Response HistoryTracking$response")
                try {
                    val `object` = JSONObject(response)
                    val code = `object`.optString("Code").toString()
                    if (code.equals("0", ignoreCase = true)) { //  Track_info.setVisibility(View.VISIBLE);
                        val jArray = `object`.getJSONArray("HistoryData")
                        val number = jArray.length()
                        val num = Integer.toString(number)
                        Log.i("history lat", num)
                        if (number == 0) {
                        } else {

                            googleMap!!.clear()
                            if (Points != null) {
                                Points.clear()
                            }

                            for (i in 0 until 30) {
                                val json_data = jArray.getJSONObject(i)
                                a_VehicleNo = json_data.getString("Vehicle").toString()
                                lat = json_data.getString("Latitude").toString()
                                lng = json_data.getString("Longitude").toString()
                                Lo = json_data.getString("Location").toString()
                                DT = json_data.getString("Datetime").toString()
                                Dire = json_data.getString("Speed").toString()
                                Ign = json_data.getString("Ignition").toString()
                                vehicleT = json_data.getString("VehicleType").toString()
                                latt = lat!!.toDouble()
                                lngg = lng!!.toDouble()
                                position = LatLng(latt, lngg)
                                Locatn_time.add("" + DT)
                                Points.add(position!!)
                                googleMap!!.addMarker(MarkerOptions()
                                        .position(position!!).title(vehicle)
                                        .snippet(Lo).icon(BitmapDescriptorFactory.fromResource(R.drawable.points)))

                            }
                            pDialog!!.dismiss()
                            drawPolyLineOnMap(Points)

                            Log.i("history la", Points.toString())
                            googleMap!!.addMarker(MarkerOptions()
                                    .position(Points.get(0)).title("Start")
                                    .snippet(Locatn_time.get(0)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_start)))
                            googleMap!!.addMarker(MarkerOptions()
                                    .position(Points.get(Points.size - 1)).title("End")
                                    .snippet(Locatn_time.get(Points.size - 1)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_stop)))


                        }
                    } else {
                        googleMap!!.clear()
                        pDialog!!.dismiss()

                        var alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@PArentHistoryTrackingDetails)
                        alertDialog.setTitle("Alert")
                        alertDialog.setMessage("" + "No Data Found")
                        alertDialog.setIcon(R.drawable.alert)
                        alertDialog.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
                        alertDialog.show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error ->
                Log.e("VOLLEY", error.toString())
                pDialog!!.dismiss()
            }) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                @RequiresApi(Build.VERSION_CODES.KITKAT)
                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    return mRequestBody?.toByteArray(StandardCharsets.UTF_8)
                } //
            }
            stringRequest.retryPolicy = DefaultRetryPolicy(
                    25000000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            VolleySingleton.getInstance().getRequestQueue().add(stringRequest)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun setdate() {
        val simpledateformat = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val from_date = Date()
        val to_date = from_date.time - 1440 * 60 * 1000
        Log.e("TEST", from_date.time.toString() + " - " + to_date)
        default_From_date = simpledateformat.format(to_date)
        default_To_date = simpledateformat.format(from_date)
        default_mode = "Both"
        Log.e("from", default_From_date)
        Log.e("to", default_To_date)
    }


    class CustomInfoWindow(inflater: LayoutInflater) : GoogleMap.InfoWindowAdapter {
        internal var inflater: LayoutInflater? = null


        init {
            this.inflater = inflater
        }

        override fun getInfoWindow(marker: Marker): View? {
            if (marker != null) {
                if (markerPlaces.containsAll(Collections.singleton(marker.getId()))) {
                    val view1 = inflater!!.inflate(R.layout.custommapview_vechilepoints, null)
                    // Getting the position from the marker
                    val latLng = marker.getPosition()
                    val title = marker.getTitle()
                    val str2 = title.split(("_").toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                    try {
                        view1.tv_heirarchynamme.setText(str2[0])
                        view1.tv_ps.setText(str2[1])
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                    return (view1)

                }

            }
            return null
        }


        override fun getInfoContents(marker: Marker): View? {
            return null
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMapp: GoogleMap?) {
        googleMap = googleMapp
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val criteria = Criteria()
        val location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false))
        if (location != null) {
            googleMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.getLatitude(), location.getLongitude()), 13f))
            val cameraPosition = CameraPosition.Builder()
                    .target(LatLng(location.getLatitude(), location.getLongitude())) // Sets the center of the map to location user
                    .zoom(17f) // Sets the zoom
                    // Sets the orientation of the camera to east
                    // Sets the tilt of the camera to 30 degrees
                    .build() // Creates a CameraPosition from the builder
            googleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }

    }

    private fun fromdata() {

        // Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_LONG).show();
        val alertDialog = android.app.AlertDialog.Builder(this@PArentHistoryTrackingDetails).create()
        //  alertDialog.setTitle("Select Date and time");
        //  alertDialog.setTitle("Select Date and time");
        val inflater = this@PArentHistoryTrackingDetails.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_datetime, null)
        alertDialog.setView(dialogView)
        val tv = TextView(this@PArentHistoryTrackingDetails)
        val fm = EditText(this@PArentHistoryTrackingDetails)
        val toT = EditText(this@PArentHistoryTrackingDetails)
        val spnr = Spinner(this@PArentHistoryTrackingDetails)
        val spnr1 = Spinner(this@PArentHistoryTrackingDetails)
        fm.inputType = InputType.TYPE_NULL
        toT.inputType = InputType.TYPE_NULL
        tv.text = "Select Date"
        // tv.setGravity(0);
        tv.gravity = Gravity.CENTER
        tv.textSize = 19f
        tv.setTextColor(Color.BLACK)
        fm.setText(default_From_date)
        toT.setText(default_To_date)


        Start_time = fm.text.toString()
        Stop_time = toT.text.toString()
        Log.e("Start_time", Start_time)
        Log.e("Stop_time", Stop_time)

        custom = CustomDateTimePicker(this@PArentHistoryTrackingDetails,
                object : CustomDateTimePicker.ICustomDateTimeListener {
                    override fun onSet(dialog: Dialog?, calendarSelected: Calendar,
                                       dateSelected: Date?, year: Int, monthFullName: String?,
                                       monthShortName: String?, monthNumber: Int, date: Int,
                                       weekDayFullName: String?, weekDayShortName: String?,
                                       hour24: Int, hour12: Int, min: Int, sec: Int,
                                       AM_PM: String?) { //
//                        Toast.makeText(getApplicationContext(), "" + year
//                                + "-" + (monthNumber + 1) + "-" + calendarSelected.get(Calendar.DAY_OF_MONTH)
//                                + " " + hour24 + ":" + min
//                                + ":" + sec, Toast.LENGTH_LONG).show();
//                        ((TextInputEditText) findViewById(R.id.edtEventDateTime))
                        fm.setText("")
                        fm.setText(year
                                .toString() + "-" + (monthNumber + 1) + "-" + calendarSelected[Calendar.DAY_OF_MONTH]
                                + " " + hour24 + ":" + min
                        )
                    }

                    override fun onCancel() {}
                })
        custom!!.set24HourFormat(true)
        /**
         * Pass Directly current data and time to show when it pop up
         */
        custom!!.setDate(Calendar.getInstance())
        custom1 = CustomDateTimePicker(this@PArentHistoryTrackingDetails,
                object : CustomDateTimePicker.ICustomDateTimeListener {
                    override fun onSet(dialog: Dialog?, calendarSelected: Calendar,
                                       dateSelected: Date?, year: Int, monthFullName: String?,
                                       monthShortName: String?, monthNumber: Int, date: Int,
                                       weekDayFullName: String?, weekDayShortName: String?,
                                       hour24: Int, hour12: Int, min: Int, sec: Int,
                                       AM_PM: String?) { //
//                        Toast.makeText(this, "" + year
//                                + "-" + (monthNumber + 1) + "-" + calendarSelected.get(Calendar.DAY_OF_MONTH)
//                                + " " + hour24 + ":" + min
//                                + ":" + sec, Toast.LENGTH_LONG).show();
//                        ((TextInputEditText) findViewById(R.id.edtEventDateTime))
                        toT.setText("")
                        toT.setText(year
                                .toString() + "-" + (monthNumber + 1) + "-" + calendarSelected[Calendar.DAY_OF_MONTH]
                                + " " + hour24 + ":" + min
                        )
                    }

                    override fun onCancel() {}
                })
        custom1!!.set24HourFormat(true)
        /**
         * Pass Directly current data and time to show when it pop up
         */
        custom1!!.setDate(Calendar.getInstance())
        fm.setOnClickListener {
            //custom.showDialog();
            val currentDate = Calendar.getInstance()
            date_fm = Calendar.getInstance()
            val DatePickerDialog1 = DatePickerDialog(this@PArentHistoryTrackingDetails,
                    OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        // view.setMaxDate(System.currentTimeMillis());
                        view.maxDate = currentDate.timeInMillis
                        date_fm!!.set(year, monthOfYear, dayOfMonth)
                        TimePickerDialog(this@PArentHistoryTrackingDetails, OnTimeSetListener { view, hourOfDay, minute ->
                            date_fm!!.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            date_fm!!.set(Calendar.MINUTE, minute)

                            var df: DateFormat? = null
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                df = android.icu.text.SimpleDateFormat("yyyy-MM-dd hh:mm")
                            }
                            fm.setText("")
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                fm.setText("" + df!!.format(date_fm!!.getTime()))
                            }
                        }, currentDate[Calendar.HOUR_OF_DAY], currentDate[Calendar.MINUTE], false).show()
                    }, currentDate[Calendar.YEAR], currentDate[Calendar.MONTH], currentDate[Calendar.DATE])
            DatePickerDialog1.datePicker.maxDate = System.currentTimeMillis()
            DatePickerDialog1.show()
        }
        toT.setOnClickListener {
            // custom1.showDialog();
            val currentDate = Calendar.getInstance()
            date_to = Calendar.getInstance()
            val DatePickerDialog2 = DatePickerDialog(this@PArentHistoryTrackingDetails, OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                date_to!!.set(year, monthOfYear, dayOfMonth)
                TimePickerDialog(this@PArentHistoryTrackingDetails, OnTimeSetListener { view, hourOfDay, minute ->
                    date_to!!.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    date_to!!.set(Calendar.MINUTE, minute)
                    val df = SimpleDateFormat("yyyy-MM-dd hh:mm")
                    toT.setText("")
                    toT.setText("" + df.format(date_to!!.getTime()))
                }, currentDate[Calendar.HOUR_OF_DAY], currentDate[Calendar.MINUTE], false).show()
            }, currentDate[Calendar.YEAR], currentDate[Calendar.MONTH], currentDate[Calendar.DATE])
            DatePickerDialog2.datePicker.maxDate = System.currentTimeMillis()
            DatePickerDialog2.show()
        }
        //        fm.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
//        toT.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        val ll = LinearLayout(this@PArentHistoryTrackingDetails)
        ll.orientation = LinearLayout.VERTICAL
        ll.addView(tv)
        ll.addView(fm)
        ll.addView(toT)
        //ll.addView(spnr);
//        ll.addView(spnr1);
        alertDialog!!.setView(ll)
        alertDialog!!.setButton(AlertDialog.BUTTON_POSITIVE, "Submit"
        ) { dialog, which ->
            default_From_date = fm.text.toString()
            default_To_date = toT.text.toString()
            Log.e("changed_from", default_From_date)
            Log.e("changed_to", default_To_date)
            Log.e("changed_mode", default_mode)
            //  makeJsonObjReq();
            HistoryTracking1(VechileId!!, default_From_date!!, default_To_date!!)

            dialog.dismiss()
        }
        alertDialog!!.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel"
        ) { dialog, which -> dialog.dismiss() }
        alertDialog!!.setCancelable(false)
        alertDialog!!.show()
    }

    private fun makeJsonObjReq1() {
        try {


            val jsonBody = JSONObject()

            jsonBody.put("VehicleId", VechileId)
            val mRequestBody = jsonBody.toString()
            val stringRequest: StringRequest = object : StringRequest(Method.POST, url1, Response.Listener { response ->
                Log.i("VOLLEY", "TabActivity$response")
                try {
                    val `object` = JSONObject(response)
                    val code = `object`.optString("Code").toString().toInt()
                    val message = `object`.optString("Message").toString()
                    Log.i(" RESPONSE ", code.toString() + message)
                    //   Toast.makeText(this@PArentHistoryTrackingDetails, " RESPONSE "+ code+message, Toast.LENGTH_LONG).show();
                    if (code == 0) { //
                        val jArray = `object`.getJSONArray("LivevehicleDetails")
                        val number = jArray.length()
                        val num = Integer.toString(number)
                        for (i in 0 until number) {
                            val json_data = jArray.getJSONObject(i)
                            val VehicleName = json_data.getString("VehicleName").toString()

                        }
                    } else {
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error -> Log.e("VOLLEY", error.toString()) }) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    return mRequestBody?.toByteArray(StandardCharsets.UTF_8)
                }
            }
            VolleySingleton.getInstance().requestQueue.add(stringRequest)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun drawPolyLineOnMap(list: List<LatLng?>) {
        Log.e("histry", list.toString())
//
//        polyOptions = PolylineOptions()
//        polyOptions!!.color(Color.BLUE)
//        polyOptions!!.width(5f)
//        polyOptions!!.addAll(list)
//        polyOptions!!.geodesic(true)
//        googleMap!!.addPolyline(polyOptions)

        line = googleMap!!.addPolyline(PolylineOptions()
                .addAll(list)
                .width(5f)
                .color(Color.BLUE)
                .geodesic(true))

    }


}
