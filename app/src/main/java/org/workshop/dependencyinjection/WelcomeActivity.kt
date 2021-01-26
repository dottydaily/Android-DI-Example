package org.workshop.dependencyinjection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.workshop.dependencyinjection.databinding.ActivityWelcomeBinding
import org.workshop.dependencyinjection.service_playground.CountdownService
import org.workshop.dependencyinjection.service_playground.ServicePlaygroundActivity

class WelcomeActivity : AppCompatActivity() {

    // ViewBinding
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.gotoGameActivityButton.setOnClickListener {
            startNewPage(MainActivity::class.java)
        }

        binding.runStartedServiceButton.setOnClickListener {
            val intent = Intent(this, CountdownService::class.java).apply {
                putExtra(CountdownService.START_COUNTDOWN_IMMEDIATELY, true)
            }
            startService(intent)
        }

        binding.gotoServicePlaygroundButton.setOnClickListener {
            startNewPage(ServicePlaygroundActivity::class.java)
        }

    }

    fun startNewPage(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }
}