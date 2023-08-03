package com.app.ecolive.taximodule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.app.ecolive.R
import com.app.ecolive.databinding.ActivityTaxiHomeBinding
import com.app.ecolive.taximodule.fragment.ActivityFragment
import com.app.ecolive.taximodule.fragment.FragmentSchudle
import com.app.ecolive.taximodule.fragment.HomeFragment
import com.app.ecolive.utils.Utils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng

class TaxiHomeActivity : AppCompatActivity()  {
    lateinit var binding :ActivityTaxiHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_taxi_home)
        Utils.changeStatusColor(this, R.color.black)
        Utils.changeStatusTextColor(this)

        if (savedInstanceState == null) {
            val newFragment: Fragment = HomeFragment()
            var far =supportFragmentManager.beginTransaction().replace(R.id.fragment,newFragment).commit()
        }
        binding.activityll.setOnClickListener {
            val newFragment: Fragment = ActivityFragment()
            var far =supportFragmentManager.beginTransaction().replace(R.id.fragment,newFragment).commit()
        }
        binding.homell.setOnClickListener {
            val newFragment: Fragment = HomeFragment()
            var far =supportFragmentManager.beginTransaction().replace(R.id.fragment,newFragment).commit()
        }

        binding.schudlell.setOnClickListener {
            val newFragment: Fragment = FragmentSchudle()
            var far =supportFragmentManager.beginTransaction().replace(R.id.fragment,newFragment).commit()
        }
    }


}