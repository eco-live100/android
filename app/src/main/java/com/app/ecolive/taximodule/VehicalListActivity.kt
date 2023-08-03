package com.app.ecolive.taximodule

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.app.ecolive.R
import com.app.ecolive.databinding.ActivityTaxiHomeBinding
import com.app.ecolive.databinding.ActivityVehicalListBinding
import com.app.ecolive.utils.Utils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class VehicalListActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var binding : ActivityVehicalListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_vehical_list)
        Utils.changeStatusColor(this, R.color.black)
        Utils.changeStatusTextColor(this)
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.taximap2) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        binding.paymentOption.setOnClickListener{
            startActivity(Intent(this,TaxiPaymentActivity::class.java))
        }
    }

    override fun onMapReady(mMap: GoogleMap) {
        val latLng = LatLng(28.4747789, 77.0419619)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F));
    }
}