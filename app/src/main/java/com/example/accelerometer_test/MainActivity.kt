package com.example.accelerometer_test

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var ball: TextView
    private lateinit var upDownText: TextView
    private lateinit var rightLeftText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ball = findViewById(R.id.tv_ball)
        upDownText = findViewById(R.id.tv_upDown_value)
        rightLeftText = findViewById(R.id.tv_rightLeft_value)

        setupSensor()
    }

    private fun setupSensor() {

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_FASTEST,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {

        if(event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val rightLeft = event.values[0]
            val upDown = event.values[1]

            Log.i("Sensor values", "Sensor value rightLeft $rightLeft")
            Log.i("Sensor values", "Sensor value upDown $upDown")

            ball.apply {
                translationX = rightLeft * -100
                translationY = upDown * 100
            }

            if (rightLeft >= 4.65 || rightLeft <= -4.65) {
                rightLeftText.setText("Reached side boundaries!!")
            } else {
                rightLeftText.setText("rightLeft = $rightLeft")
            }

            if (upDown >= 9.15 || upDown <= -9.15) {
                upDownText.setText("Reached height boundaries!!")
            } else {
                upDownText.setText("upDown = $upDown")
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        return
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }
}