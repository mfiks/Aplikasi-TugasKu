package com.example.tugasku.activity

import android.os.Bundle
import com.example.tugasku.databinding.ActivityAllBinding

class AllActivity : BaseActivity() {

    private lateinit var binding: ActivityAllBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}