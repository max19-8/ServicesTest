package com.example.servicestest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.servicestest.background.MyForegroundService
import com.example.servicestest.background.MyService
import com.example.servicestest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by  lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.serviceButton.setOnClickListener {
            startService(MyService.newIntent(this))
        }
        binding.foregroundServiceButton.setOnClickListener {
            ContextCompat.startForegroundService(this,MyForegroundService.newIntent(this))
        }
    }
}