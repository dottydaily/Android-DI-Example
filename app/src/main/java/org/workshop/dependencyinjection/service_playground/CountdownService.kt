package org.workshop.dependencyinjection.service_playground

import android.app.Service
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

class CountdownService : Service() {
    companion object {
        const val START_COUNTDOWN_IMMEDIATELY = "START_COUNTDOWN_IMMEDIATELY"
    }

    // Class used for the client Binder.
    inner class CountdownBinder: Binder() {
        fun getService(): CountdownService = this@CountdownService
    }

    // Binder object
    private val binder = CountdownBinder()

    // LiveData for checking the status.
    private val _statusLiveData = MutableLiveData<Boolean>().apply { value = null }
    val statusLiveData: LiveData<Boolean> get() = _statusLiveData

    // LiveData for counting the number in second.
    private val _numberLiveData = MutableLiveData<Int>().apply { value = null }
    val numberLiveData: LiveData<Int> get() = _numberLiveData

    // This service startId
    var thisServiceStartId: Int? = null

    val serviceScope = CoroutineScope(Dispatchers.IO)

    // The service is being created
    override fun onCreate() {
        super.onCreate()
        Log.d("DependencyInjection", "onCreate() of CountdownService's object")
    }

    // The service is starting, due to a call to startService()
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Start CountdownService", Toast.LENGTH_SHORT).show()
        thisServiceStartId = startId

        intent?.let {
            val neededToStartCountdown = it.getBooleanExtra(START_COUNTDOWN_IMMEDIATELY, false)
            if (neededToStartCountdown) {
                startCountdown()
            }
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        // A client is binding to the service with bindService()
        return binder
    }

    // All clients have unbound with unbindService()
    override fun unbindService(conn: ServiceConnection) {
        super.unbindService(conn)
    }

    // A client is binding to the service with bindService(),
    // after onUnbind() has already been called
    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
    }

    // The service is no longer used and is being destroyed
    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
        if (serviceScope.isActive) {
            serviceScope.cancel()
        }
        Log.d("DependencyInjection", "onDestroy() of CountdownService's object")
    }

    fun startCountdown() {
        Log.d("DependencyInjection", "Start countdown 1 to 10")

        _statusLiveData.postValue(null)
        serviceScope.launch {
            repeat(10) {
                delay(1000L)
                _numberLiveData.postValue(it+1)
                Log.d("DependencyInjection", "CountdownService : ${it+1}")
            }

            _statusLiveData.postValue(true)
            Log.d("DependencyInjection", "Countdown finished!!")

            // This must be done when this service is Started service.
            // to ensure that this service will be stop.
            // We don't do this when only using this service as Bound service.
            // Because the Bound service will stop itself when lifetime of its app component has end.
            thisServiceStartId?.let { stopSelf(it) }
        }
    }
}