package com.delight.whatsweather.utils

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment


class PermissionsUtils {
    companion object {
        const val REQUEST_CODE = 400

        fun newInstance(): LocationSettingDialog {
            return LocationSettingDialog()
        }
    }

    /**
     * Runtime Permission Check
     */
    fun requestPermission(activity: AppCompatActivity?) {
        ActivityCompat.requestPermissions(
            activity!!, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            REQUEST_CODE
        )
    }

    /**
     * Common methods to determine if a location permission is granted
     */
    fun isPermissionGranted(
        grantPermissions: Array<String?>,
        grantResults: IntArray
    ): Boolean {
        val permissionSize = grantPermissions.size
        for (i in 0 until permissionSize) {
            if (Manifest.permission.ACCESS_FINE_LOCATION == grantPermissions[i]) {
                return grantResults[i] == PackageManager.PERMISSION_GRANTED
            }
        }
        return false
    }

    /**
     * Dialog extension for permissions
     */
    class LocationSettingDialog : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return AlertDialog.Builder(getActivity())
                .setMessage("You need to set your device's location.")
                .setPositiveButton("Confirm"
                ) { dialogInterface, i ->
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }.create()
        }


    }

}