package org.workshop.dependencyinjection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.workshop.dependencyinjection.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // ViewBinding
    private lateinit var binding: ActivityMainBinding

    // ViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}