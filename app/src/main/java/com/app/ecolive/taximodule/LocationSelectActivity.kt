package com.app.ecolive.taximodule

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.app.ecolive.R
import com.app.ecolive.databinding.ActivityLocationSelectBinding
import com.app.ecolive.utils.Utils

class LocationSelectActivity : AppCompatActivity() {
    lateinit var binding :ActivityLocationSelectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_location_select)
        Utils.changeStatusColor(this, R.color.black)
        Utils.changeStatusTextColor(this)
        binding.toolbar.toolbarTitle.text ="Select a location"
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.AddStop.setOnClickListener {
            startActivity(Intent(this,AddStopActivity::class.java))
        }
    }
}