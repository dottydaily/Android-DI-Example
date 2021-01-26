package org.workshop.dependencyinjection.service_playground

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import org.workshop.dependencyinjection.R
import org.workshop.dependencyinjection.databinding.ActivityServicePlaygroundBinding

class ServicePlaygroundActivity : AppCompatActivity() {

    // ViewBinding
    private lateinit var binding: ActivityServicePlaygroundBinding

    // Service
    private lateinit var countdownService: CountdownService
    private var isBounding = false

    // ServiceConnection - Defines callbacks for service binding, passed to bindService()
    private val connection = object: ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, service: IBinder?) {
            // Bounded to CountdownService, cast the IBinder and get CountdownService instance.
            val binder = service as CountdownService.CountdownBinder
            countdownService = binder.getService()
            isBounding = true

            observeCountdownLiveDataFromService()
            observeStatusLiveDataFromService()

            Log.d("DependencyInjection", "Bounded to CountdownService")
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isBounding = false
            countdownService.numberLiveData.removeObservers(this@ServicePlaygroundActivity)
            countdownService.statusLiveData.removeObservers(this@ServicePlaygroundActivity)

            Log.d("DependencyInjection", "Unbounded from CountdownService")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServicePlaygroundBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bind to CountdownService
        Intent(this, CountdownService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }

        handleStartButton()
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
        isBounding = false
    }

    private fun handleStartButton() {
        binding.startButton.setOnClickListener {
            if (isBounding) {
                countdownService.startCountdown()
            }
        }
    }

    private fun observeCountdownLiveDataFromService() {
        countdownService.numberLiveData.observe(this) {
            it?.let {
                binding.countdownTextView.text = "$it"
            }
        }
    }

    private fun observeStatusLiveDataFromService() {
        countdownService.statusLiveData.observe(this) {
            it?.let { isDone ->
                binding.countdownTextView.text = if (isDone) "Done" else "Failed"
            }
        }
    }
}