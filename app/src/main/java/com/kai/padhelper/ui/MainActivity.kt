package com.kai.padhelper.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.kai.padhelper.databinding.ActivityMainBinding
import com.kai.padhelper.ui.viewmodels.RecordViewModel
import com.kai.padhelper.ui.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val searchViewModel: SearchViewModel by viewModels()
    val recordViewModel: RecordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navHostFragment.post {
            val navController = binding.navHostFragment.findNavController()
            binding.bottomNavigationView
                .setupWithNavController(navController)
        }
    }
}