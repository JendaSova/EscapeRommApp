package com.example.escaperoomapp

import android.app.Application
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.escaperoomapp.di.appModule
import com.example.escaperoomapp.navigation.NavGraph
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class EscapeRoomApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@EscapeRoomApp)
            modules(appModule)
        }
    }
}

class MainActivity : ComponentActivity() {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private lateinit var shakeDetector: ShakeDetector
    var onShakeCallback: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        shakeDetector = ShakeDetector {
            onShakeCallback?.invoke()
        }
        setContent {
            val navController = rememberNavController()
            NavGraph(
                navController = navController,
                onShakeCallback = { callback -> onShakeCallback = callback }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.let {
            sensorManager.registerListener(shakeDetector, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(shakeDetector)
    }
}
