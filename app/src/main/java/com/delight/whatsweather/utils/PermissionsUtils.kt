package com.delight.whatsweather.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.delight.whatsweather.R


class PermissionsUtils(private var pActivity: Activity) {
    companion object {
        fun instance(pActivity: Activity): PermissionsUtils {
            return PermissionsUtils(pActivity)
        }
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
            MyLocationService.PERMISSIONS_REQUEST_CODE
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


}

