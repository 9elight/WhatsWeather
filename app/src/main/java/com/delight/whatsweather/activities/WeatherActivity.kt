package com.delight.whatsweather.activities

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.delight.whatsweather.R
import com.delight.whatsweather.data.WeatherRepositories
import com.delight.whatsweather.fragments.DetailFragment
import com.delight.whatsweather.fragments.WeatherFragment
import com.delight.whatsweather.utils.SwipeDirectionDetector
import com.delight.whatsweather.utils.broadcastReciever.ConnectivityListener
import com.delight.whatsweather.utils.broadcastReciever.NetworkChangeReceiver
import com.delight.whatsweather.views.MainActivityView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import moxy.MvpAppCompatActivity
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext


class WeatherActivity() : MvpAppCompatActivity(), MainActivityView,ConnectivityListener,CoroutineScope {
    private val manager = supportFragmentManager
    private val weatherRepositories: WeatherRepositories by inject()
    private var mDirection: SwipeDirectionDetector.Direction? = null
    private lateinit var mSwipeDetector: SwipeDirectionDetector
    private  var networkState: Boolean? = null

    private fun setConnectivityListener(listener: ConnectivityListener){
        NetworkChangeReceiver.connectivityListener = listener
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mSwipeDetector =
            object : SwipeDirectionDetector(context = applicationContext) {
                override fun onDirectionDetected(direction: Direction) {
                    mDirection = direction
                }
            }
        openWeatherFragment()
    }

    override fun onResume() {
        super.onResume()
        setConnectivityListener(this)
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let { mSwipeDetector.onTouchEvent(it) }
        if (mDirection != null && event?.action == MotionEvent.ACTION_UP)
            when (mDirection) {
                SwipeDirectionDetector.Direction.LEFT -> {
                    openDetailFragment()
                }
                SwipeDirectionDetector.Direction.RIGHT ->{
                    closeDetailFragment()
                }
                SwipeDirectionDetector.Direction.NOT_DETECTED -> Log.e("tag", "Not detected")
                SwipeDirectionDetector.Direction.UP -> Log.e("tag", "Up")
                SwipeDirectionDetector.Direction.DOWN -> Log.e("tag", "Down")
            }

        return super.onTouchEvent(event)
    }


    override fun openWeatherFragment() {
        if (!getConnectionState()){
            Toast.makeText(this, "not connection", Toast.LENGTH_SHORT).show()
        }else{
            manager.beginTransaction()
                .add(R.id.container_weather, WeatherFragment.instance())
                .commit()
        }

    }

    override fun openDetailFragment() {
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

    override fun closeDetailFragment() {
        manager.popBackStack()
        container_detail.visibility = View.GONE
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        openWeatherFragment()
    }

    private fun getConnectionState(): Boolean{
        return NetworkChangeReceiver.isConnected(this)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

}
