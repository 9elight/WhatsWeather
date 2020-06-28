package com.delight.whatsweather.activities

import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.delight.whatsweather.R
import com.delight.whatsweather.data.WeatherRepositories
import com.delight.whatsweather.fragments.DetailFragment
import com.delight.whatsweather.fragments.WeatherFragment
import com.delight.whatsweather.presenter.MainPresenter
import com.delight.whatsweather.utils.*
//import com.delight.whatsweather.utils.LocationService
import com.delight.whatsweather.utils.broadcastReciever.ConnectivityListener
import com.delight.whatsweather.utils.broadcastReciever.NetworkChangeReceiver
import com.delight.whatsweather.views.MainActivityView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext


class MainActivity() : MvpAppCompatActivity(), MainActivityView,
    ConnectivityListener, SharedPreferences.OnSharedPreferenceChangeListener,CoroutineScope {
    //region values
    private val manager = supportFragmentManager
    private val weatherRepositories: WeatherRepositories by inject()
    private var mDirection: SwipeDirectionDetector.Direction? = null
    private lateinit var mSwipeDetector: SwipeDirectionDetector
    private var networkState: Boolean? = null
    private lateinit var location:Location
    private lateinit var locationUtils: LocationUtils
    private var foregroundOnlyLocationServiceBound = false
    private var foregroundOnlyLocationService: ForegroundOnlyLocationService? = null
    private lateinit var foregroundOnlyBroadcastReceiver: ForegroundOnlyBroadcastReceiver
    private lateinit var sharedPreferences:SharedPreferences
    @InjectPresenter
    lateinit var mainPresenter: MainPresenter
//endregion
    @ProvidePresenter
    fun provideMainPresenter(): MainPresenter {
        return MainPresenter(
            weatherRepositories = weatherRepositories)
    }
    private val foregroundOnlyServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ForegroundOnlyLocationService.LocalBinder
            foregroundOnlyLocationService = binder.service
            foregroundOnlyLocationServiceBound = true
            val enabled = sharedPreferences.getBoolean(SharedPreferenceUtil.KEY_FOREGROUND_ENABLED,false)
            if (enabled){
                foregroundOnlyLocationService?.unsubscribeToLocationUpdates()
            }else{
                if (locationUtils.checkPermissions()){
                    foregroundOnlyLocationService?.subscribeToLocationUpdates()
                        ?: Log.d("MainActivity","Service Not Bound")
                }else{
                    locationUtils.requestPermissions()
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            foregroundOnlyLocationService = null
            foregroundOnlyLocationServiceBound = false
        }
    }

    private fun setConnectivityListener(listener: ConnectivityListener) {
        NetworkChangeReceiver.connectivityListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locationUtils = LocationUtils.instance(this)
        mSwipeDetector =
            object : SwipeDirectionDetector(context = applicationContext) {
                override fun onDirectionDetected(direction: Direction) {
                    mDirection = direction
                }
            }
        foregroundOnlyBroadcastReceiver = ForegroundOnlyBroadcastReceiver()
        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE)


//        if (!locationUtils.checkPermissions()) {
//            locationUtils.requestPermissions()
//        } else {
//            locationUtils.initLocation()
//        }
    }

    override fun onStart() {
        super.onStart()
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        val serviceIntent = Intent(this,ForegroundOnlyLocationService::class.java)
        bindService(serviceIntent,foregroundOnlyServiceConnection,Context.BIND_AUTO_CREATE)

    }

    override fun onResume() {
        super.onResume()
        setConnectivityListener(this)
        LocalBroadcastManager.getInstance(this).registerReceiver(
            foregroundOnlyBroadcastReceiver,
            IntentFilter(
                ForegroundOnlyLocationService.ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
        )
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
            foregroundOnlyBroadcastReceiver
        )
        super.onPause()
    }

    override fun onStop() {
        if (foregroundOnlyLocationServiceBound) {
            unbindService(foregroundOnlyServiceConnection)
            foregroundOnlyLocationServiceBound = false
        }
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)

        super.onStop()

    }
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let { mSwipeDetector.onTouchEvent(it) }
        if (mDirection != null && event?.action == MotionEvent.ACTION_UP)
            when (mDirection) {
                SwipeDirectionDetector.Direction.LEFT -> {
                    showSecondScreenState()
                }
                SwipeDirectionDetector.Direction.RIGHT -> {
                    manager.popBackStack()
                    container_detail.visibility = View.GONE
                }
                SwipeDirectionDetector.Direction.NOT_DETECTED -> Log.e("tag", "Not detected")
                SwipeDirectionDetector.Direction.UP -> Log.e("tag", "Up")
                SwipeDirectionDetector.Direction.DOWN -> Log.e("tag", "Down")
            }

        return super.onTouchEvent(event)
    }


    override fun showFirstScreenState(location: Location?) {
        progress_main.visibility = View.GONE
        val weatherFragment = WeatherFragment.instance(location)
        if (!getConnectionState()) {
            Toast.makeText(this, "not connection", Toast.LENGTH_SHORT).show()
        } else {
            manager.beginTransaction()
                .add(R.id.container_weather, weatherFragment)
                .commit()
        }
    }

    override fun showSecondScreenState() {
        progress_main.visibility = View.GONE
        if (container_detail.visibility == View.GONE) {
            container_detail.visibility = View.VISIBLE
            manager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                .replace(R.id.container_detail, DetailFragment.instance())
                .addToBackStack(null)
                .commit()
            Log.e("fragment_Detail", "open")
        }
    }

    override fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun hideLoadingScreen() {
        progress_main.visibility = View.GONE
    }


    override fun onNetworkConnectionChanged(isConnected: Boolean) {

    }

    private fun getConnectionState(): Boolean {
        return NetworkChangeReceiver.isConnected(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.i("tag", "onRequestPermissionResult")
        if (requestCode == LocationUtils.PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> {
                    Log.i("tag", "User interaction was cancelled.")
                }
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    foregroundOnlyLocationService?.subscribeToLocationUpdates()
                }
                else -> {
                    Log.e("tag","onRequestPermissionsResult")
                }
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private inner class ForegroundOnlyBroadcastReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val location = intent.getParcelableExtra<Location>(
                ForegroundOnlyLocationService.EXTRA_LOCATION
            )

            if (location != null) {
                Toast.makeText(this@MainActivity, "location ${location.toText()}", Toast.LENGTH_SHORT).show()
                showFirstScreenState(location)
            }
            }
        }

}
