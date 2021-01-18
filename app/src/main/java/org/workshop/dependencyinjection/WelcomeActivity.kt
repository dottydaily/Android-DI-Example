package org.workshop.dependencyinjection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.workshop.dependencyinjection.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    // ViewBinding
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.gotoGameActivity.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}