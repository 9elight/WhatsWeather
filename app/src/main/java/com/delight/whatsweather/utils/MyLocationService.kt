package com.delight.whatsweather.utils

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.delight.whatsweather.staticData.Constants.ALERT_TASK_CODE
import com.delight.whatsweather.staticData.Constants.LOCATION_SERVICE_ACTION
import com.delight.whatsweather.staticData.Constants.LOCATION_TASK_CODE
import com.delight.whatsweather.staticData.Constants.LOCATION_VALUE
import com.delight.whatsweather.staticData.Constants.CODE_VALUE
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

class MyLocationService() :
    Service(),
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    com.google.android.gms.location.LocationListener {

    lateinit var mGoogleApiClient: GoogleApiClient
    private var mLocation: Location? = null
    private var mLocationManager: LocationManager? = null
    private var mLocationRequest: LocationRequest? = null
    private val sUINTERVAL = (2 * 1000).toLong()  /* 10 secs */
    private val sFINTERVAL: Long = 2000 /* 2 sec */
    private lateinit var locationManager: LocationManager
    private val myBinder: MyBinder = MyBinder()
    private val intent = Intent(LOCATION_SERVICE_ACTION)

    override fun onCreate() {
        super.onCreate()
    }

    override fun onLocationChanged(location: Location) {
        mLocation = location

    }

    override fun onConnectionSuspended(p0: Int) {
        Log.e("tag", "onConnectionSuspended")
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.e("location", "locationError")
    }

    @SuppressLint("MissingPermission")
    override fun onConnected(pLocationChanged: Bundle?) {
        startLocationUpdates()
        val intent = Intent(LOCATION_SERVICE_ACTION)
        val fusedLocationProviderClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                mLocation = location
//                    mCallback.getLocation(location)
                intent.putExtra(CODE_VALUE, LOCATION_TASK_CODE)
                intent.putExtra(LOCATION_VALUE, location)
                sendBroadcast(intent)
                Log.e("tag", location.toString())
                stopSelf()
            }
        }
    }

    fun getLastLocation(): Location? {
        return mLocation
    }

    private fun initLocation(): Boolean {
        mGoogleApiClient = GoogleApiClient.Builder(this).apply {
            addConnectionCallbacks(this@MyLocationService)
            addOnConnectionFailedListener(this@MyLocationService)
            addApi(LocationServices.API)
        }.build()
        mGoogleApiClient.connect()
        mLocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return checkLocation()
    }

    private fun checkLocation(): Boolean {
        if (!isLocationEnabled()) {
            intent.putExtra(CODE_VALUE, ALERT_TASK_CODE)
            sendBroadcast(intent)
//            mCallback.showAlert(this.getString(R.string.location_title),  this.getString(R.string.location_message))

        }
        return isLocationEnabled()
    }

    private fun isLocationEnabled(): Boolean {
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )

    }


    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        mLocationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = sUINTERVAL
            fastestInterval = sFINTERVAL
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
            mGoogleApiClient,
            mLocationRequest,
            this
        )

    }

    override fun onBind(intent: Intent?): IBinder? {
        return myBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        initLocation()
        return super.onStartCommand(intent, flags, startId)
    }

    companion object {
        const val PERMISSIONS_REQUEST_CODE = 34
        fun instance(): MyLocationService {
            return MyLocationService()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("location", "onDestroyService")

    }

    inner class MyBinder : Binder() {

        fun getService(): MyLocationService? {
            return this@MyLocationService
        }
    }

}
