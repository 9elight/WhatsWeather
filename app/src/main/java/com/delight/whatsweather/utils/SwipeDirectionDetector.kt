package com.delight.whatsweather.utils

import android.content.Context
import android.view.MotionEvent
import android.view.ViewConfiguration
import kotlin.math.atan2
import kotlin.math.sqrt

abstract class SwipeDirectionDetector(context: Context) {
    private var startX: Float = 0f
    private var startY: Float = 0f
    private var isDetected: Boolean = false
    private var touchSlop: Int = 0


    abstract fun onDirectionDetected(direction: Direction)

    init {
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    fun onTouchEvent(event: MotionEvent): Boolean{
        when(event.action){
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
            }
            MotionEvent.ACTION_CANCEL,MotionEvent.ACTION_UP ->{
                if (!isDetected){
                    onDirectionDetected(Direction.NOT_DETECTED)
                }
                startX = 0.0f
                startY = 0.0f
                isDetected = false
            }
            MotionEvent.ACTION_MOVE ->{
                if (!isDetected && getDistance(event) > touchSlop){
                    isDetected = true
                    var x = event.x
                    var y = event.y

                    val direction: Direction = getDirection(startX,startY,x,y)
                    onDirectionDetected(direction)
                }
            }
        }
        return false
    }

    private fun getDirection(x1:Float, y1: Float, x2:Float, y2: Float ): Direction{
        var angle = getAngle(x1,y1,x2,y2)
        return Direction.get(angle)
    }

    private fun getAngle(x1: Float, y1: Float, x2: Float, y2: Float): Double {
        var rad = atan2(y1-y2,x2-x1) + Math.PI
        return (rad*180/Math.PI + 180) % 360
    }

    private fun getDistance(event: MotionEvent): Float {
        var distanceSum: Float = 0f
        var dx = event.getX(0) - startX
        var dy = event.getY(0) - startY
        distanceSum += sqrt(dx*dx+dy*dy)
        return distanceSum
    }

    enum class Direction{
        NOT_DETECTED,
        UP,
        DOWN,
        LEFT,
        RIGHT;

        companion object{
            fun get(angle: Double): Direction{
                return if (inRange(angle,45f,135f)){
                    UP
                } else if (inRange(angle, 0f, 45f) || inRange(angle, 315f,360f)){
                    RIGHT
                } else if (inRange(angle,225f,315f)){
                    DOWN
                }else{
                    LEFT
                }

            }

            private fun inRange(angle: Double,init: Float, end: Float): Boolean{
                return (angle >= init) && (angle < end)
            }
        }
    }
}

