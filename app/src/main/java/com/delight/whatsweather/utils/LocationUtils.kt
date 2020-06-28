package com.delight.whatsweather.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import com.delight.whatsweather.R
import com.delight.whatsweather.activities.MainActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

class LocationUtils(private var pActivity: Activity) :
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    com.google.android.gms.location.LocationListener {

    lateinit var mGoogleApiClient: GoogleApiClient
    private var mLocation: Location? = null
    private var  mLocationManager: LocationManager? = null
    private var mLocationRequest: LocationRequest? = null
    private val sUINTERVAL = (2 * 1000).toLong()  /* 10 secs */
    private val sFINTERVAL: Long = 2000 /* 2 sec */
    private lateinit var locationManager: LocationManager
//    private val mCallback: LocationCallback = MainActivity()
    override fun onLocationChanged(location: Location) {
      Log.e("tag","onLocationChanged")
        mLocation = location
    }
    override fun onConnectionSuspended(p0: Int) {
        Log.e("tag", "onConnectionSuspended")
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.e("location","locationError")
    }

    @SuppressLint("MissingPermission")
    override fun onConnected(pLocationChanged: Bundle?) {
        startLocationUpdates()
        val fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(pActivity)
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
             location ->
                if (location != null) {
                    mLocation = location
//                    mCallback.getLocation(location)
            }
        }
    }
    fun getLastLocation():Location? = mLocation
    fun initLocation() {
        mGoogleApiClient = GoogleApiClient.Builder(pActivity).apply {
            addConnectionCallbacks(this@LocationUtils)
            addOnConnectionFailedListener(this@LocationUtils)
            addApi(LocationServices.API)
        }.build()
        mGoogleApiClient.connect()
        mLocationManager = pActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        checkLocation()
    }

    private fun checkLocation(): Boolean {
        if (!isLocationEnabled()){
            showAlert(pActivity.getString(R.string.location_title),  pActivity.getString(R.string.location_message))
        }
        return isLocationEnabled()
    }
    private fun isLocationEnabled(): Boolean {
        locationManager = pActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

    }

    private fun showAlert(pTitle: String, pMessage: String) {
        val dialog = AlertDialog.Builder(pActivity)
        dialog.apply {
            setTitle(pTitle)
            setMessage(pMessage)
            setPositiveButton(pActivity.getString(R.string.location_settings)
            ) { _, _ ->
                val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                pActivity.startActivity(myIntent)
            }
        }.show()

    }
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        mLocationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = sUINTERVAL
            fastestInterval = sFINTERVAL
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)

    }

    fun checkPermissions(): Boolean{
        val coarsePermissionState = ActivityCompat.checkSelfPermission(pActivity,
            android.Manifest.permission.ACCESS_COARSE_LOCATION)
        val finePermissionState = ActivityCompat.checkSelfPermission(pActivity,
            android.Manifest.permission.ACCESS_FINE_LOCATION)

        return coarsePermissionState == PackageManager.PERMISSION_GRANTED &&
                finePermissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(pActivity,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            LocationUtils.PERMISSIONS_REQUEST_CODE
        )
    }

    fun requestPermissions(){
        val shouldProvideRationale = ActivityCompat.
        shouldShowRequestPermissionRationale(pActivity,
            android.Manifest.permission.ACCESS_COARSE_LOCATION)

        if (shouldProvideRationale){
            Log.e("tag","requestPermission")
        }else{
            startLocationPermissionRequest()
        }
    }
    companion object{
        const val PERMISSIONS_REQUEST_CODE = 34
        fun instance(pActivity: Activity):LocationUtils{
            return LocationUtils(pActivity)
        }
    }

}