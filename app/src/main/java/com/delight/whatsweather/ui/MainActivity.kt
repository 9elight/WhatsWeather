package com.delight.whatsweather.ui

import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.delight.whatsweather.R
import com.delight.whatsweather.adapters.DailyWeatherAdapter
import com.delight.whatsweather.data.WeatherRepositories
import com.delight.whatsweather.model.onecall.Daily
import com.delight.whatsweather.presenter.MainPresenter
import com.delight.whatsweather.staticData.Constants.ALERT_TASK_CODE
import com.delight.whatsweather.staticData.Constants.LOCATION_SERVICE_ACTION
import com.delight.whatsweather.staticData.Constants.CODE_VALUE
import com.delight.whatsweather.staticData.Constants.LOCATION_TASK_CODE
import com.delight.whatsweather.staticData.Constants.LOCATION_VALUE
import com.delight.whatsweather.utils.*
//import com.delight.whatsweather.utils.LocationService
import com.delight.whatsweather.utils.broadcastReciever.ConnectivityListener
import com.delight.whatsweather.utils.broadcastReciever.NetworkChangeReceiver
import com.delight.whatsweather.views.MainActivityView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.detail_clouds_value
import kotlinx.android.synthetic.main.activity_main.detail_dew_point_value
import kotlinx.android.synthetic.main.activity_main.detail_humidity_value
import kotlinx.android.synthetic.main.activity_main.detail_pressure_value
import kotlinx.android.synthetic.main.activity_main.detail_sunrise_value
import kotlinx.android.synthetic.main.activity_main.detail_sunset_value
import kotlinx.android.synthetic.main.activity_main.detail_uvi_value
import kotlinx.android.synthetic.main.activity_main.detail_wind_speed_value
import kotlinx.android.synthetic.main.activity_main.progress_circular
import kotlinx.android.synthetic.main.activity_main.txt_city
import kotlinx.android.synthetic.main.activity_main.txt_date
import kotlinx.android.synthetic.main.activity_main.txt_description
import kotlinx.android.synthetic.main.activity_main.txt_min_temp
import kotlinx.android.synthetic.main.activity_main.weather_icon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext


class MainActivity() : MvpAppCompatActivity(), MainActivityView,
    ConnectivityListener, MyLocationCallback, OnItemClickListener,
    CoroutineScope {

    //region values
    private val manager = supportFragmentManager
    private val weatherRepositories: WeatherRepositories by inject()
    private val myMainPresenter: MainPresenter by inject()
    private var mDirection: SwipeDirectionDetector.Direction? = null
    private lateinit var mSwipeDetector: SwipeDirectionDetector
    private lateinit var permissionsUtils: PermissionsUtils
    private lateinit var adapter: DailyWeatherAdapter
    private var myLocationService: MyLocationService? = null
    private lateinit var mIntent: Intent
    private var bound: Boolean = false
    private lateinit var receiver: BroadcastReceiver

    //endregion

    @InjectPresenter
    lateinit var mainPresenter: MainPresenter

    @ProvidePresenter
    fun provideMainPresenter(): MainPresenter {
        return myMainPresenter

    }


    private val sConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MyLocationService.MyBinder
            myLocationService = binder.getService()
            bound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bound = false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        onLocationReceive()
    }

    private fun onLocationReceive() {
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.getIntExtra(CODE_VALUE, 0)) {
                    ALERT_TASK_CODE -> showAlert(
                        getString(R.string.location_title),
                        getString(R.string.location_message)
                    )
                    LOCATION_TASK_CODE -> {
                        val location = intent.getParcelableExtra<Location>(LOCATION_VALUE)
                        if (location != null) {
                            launch {
                                myMainPresenter.loadWeather(
                                    location.latitude,
                                    location.longitude,
                                    "metric"
                                )
                                myMainPresenter.showLastWeather(null)
                            }
                        }
                    }

                }
            }

        }
        val filter = IntentFilter(LOCATION_SERVICE_ACTION)
        registerReceiver(receiver, filter)
    }

    override fun onStart() {
        super.onStart()
        NetworkChangeReceiver.connectivityListener = this
        bindService(mIntent, sConnection, Context.BIND_AUTO_CREATE)
        launch {
            loadWeather()
            mainPresenter.setDailyRecycler()
        }
    }

    override fun onStop() {
        super.onStop()
        if (!bound) return
        unbindService(sConnection)
        bound = false
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let { mSwipeDetector.onTouchEvent(it) }
        if (mDirection != null && event?.action == MotionEvent.ACTION_UP)
            when (mDirection) {
                SwipeDirectionDetector.Direction.LEFT -> {
                    if (container_recycler.isVisible) {
                        container_recycler.visibility = View.GONE
                    } else {
                        showDetailFragment()
                    }
                }
                SwipeDirectionDetector.Direction.RIGHT -> {
                    if (detail_scrollview.isVisible) {
                        detail_scrollview.visibility = View.GONE
                    } else {
                        container_recycler.visibility = View.VISIBLE
                    }
                }
                SwipeDirectionDetector.Direction.NOT_DETECTED -> Log.e("tag", "Not detected")
                SwipeDirectionDetector.Direction.UP -> Log.e("tag", "Up")
                SwipeDirectionDetector.Direction.DOWN -> Log.e("tag", "Down")
            }

        return super.onTouchEvent(event)
    }

    private fun init() {
        mIntent = Intent(this, MyLocationService::class.java)
        permissionsUtils = PermissionsUtils.instance(this)
        adapter = DailyWeatherAdapter(this)
        mSwipeDetector =
            object : SwipeDirectionDetector(context = applicationContext) {
                override fun onDirectionDetected(direction: Direction) {
                    mDirection = direction
                }
            }
    }

    private fun loadWeather() {
        if (NetworkChangeReceiver.isConnected(this)) {
            if (!permissionsUtils.checkPermissions()) {
                permissionsUtils.requestPermissions()
            } else {
                startService(mIntent)
            }
        } else {
            launch {
                mainPresenter.showLastWeather(null)
            }
        }
    }


    override fun showDetailFragment() {
        if (detail_scrollview.visibility == View.GONE) {
            detail_scrollview.visibility = View.VISIBLE
            Log.e("fragment_Detail", "open")

        }
    }


    override fun startWeatherLoading() {
        txt_date.visibility = View.INVISIBLE
        txt_description.visibility = View.INVISIBLE
        txt_min_temp.visibility = View.INVISIBLE
        txt_max_temp.visibility = View.INVISIBLE
        txt_city.visibility = View.INVISIBLE
        weather_icon.visibility = View.INVISIBLE
        progress_circular.visibility = View.VISIBLE
    }

    override fun endWeatherLoading() {
        txt_date.visibility = View.VISIBLE
        txt_description.visibility = View.VISIBLE
        txt_min_temp.visibility = View.VISIBLE
        txt_max_temp.visibility = View.VISIBLE
        txt_city.visibility = View.VISIBLE
        weather_icon.visibility = View.VISIBLE
        progress_circular.visibility = View.INVISIBLE
    }


    override fun showWeather(daily: Daily, city: String) {
        val minTemp = "${daily.temp.min.toInt()}°С"
        val maxTemp = "${daily.temp.max.toInt()}°С"
        txt_date.text = DateParser.parse(daily.dt)
        txt_city.text = city
        weather_icon.setImageResource(WeatherIconHelper.get(daily.weather[0].icon))
        txt_min_temp.text = minTemp
        txt_max_temp.text = maxTemp
        txt_description.text = daily.weather[0].description
        daily.apply {
            detail_pressure_value.text = this.pressure.toString()
            detail_humidity_value.text = this.humidity.toString()
            detail_uvi_value.text = this.uvi.toString()
            detail_wind_speed_value.text = this.wind_speed.toString()
            detail_dew_point_value.text = this.dew_point.toString()
            detail_clouds_value.text = this.clouds.toString()
            detail_sunrise_value.text = this.sunrise.toString()
            detail_sunset_value.text = this.sunset.toString()
        }

    }

    override fun showError(massage: String?) {
        Toast.makeText(this, "showError $massage", Toast.LENGTH_LONG).show()
    }

    override fun setDailyRecycler(list: List<Daily>) {
        daily_recycler.adapter = adapter
        adapter.updateList(list)
    }


    override fun onNetworkConnectionChanged(isConnected: Boolean) {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.i("tag", "onRequestPermissionResult")
        if (requestCode == MyLocationService.PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> {
                    Log.i("tag", "User interaction was cancelled.")
                }
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                }
                else -> {
                    Log.e("tag", "onRequestPermissionsResult")
                }
            }
        }
    }

    override fun onBackPressed() {
        if (detail_scrollview.isVisible) {
            detail_scrollview.visibility = View.GONE
        } else if (container_recycler.isVisible) {
            container_recycler.visibility = View.GONE
        }
        super.onBackPressed()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun getLocation(location: Location) {

    }

    private fun showAlert(pTitle: String, pMessage: String) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(pTitle)
            .setMessage(pMessage)
            .setPositiveButton(
                this.getString(R.string.location_settings)
            ) { _, _ ->
                val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                this.startActivity(myIntent)
            }
            .create()
        dialog.show()
    }


    override fun onItemClick(position: Int) {
        launch {
            myMainPresenter.showLastWeather(position)
        }
    }
}



