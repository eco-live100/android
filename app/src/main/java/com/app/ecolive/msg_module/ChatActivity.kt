package com.app.ecolive.msg_module


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.app.ecolive.R
import com.app.ecolive.databinding.ChatActivityBinding
import com.app.ecolive.login_module.LoginActivity
import com.app.ecolive.rider_module.HomeRiderrActivity
import com.app.ecolive.rider_module.VehicleInfoActivity
import com.app.ecolive.utils.PopUpVehicleChoose
import com.app.ecolive.utils.PreferenceKeeper
import com.app.ecolive.utils.Utils
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec


class ChatActivity: AppCompatActivity() {
    lateinit var binding: ChatActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@ChatActivity, R.layout.chat_activity)
        setToolBar()
    }

    private fun setToolBar() {
        Utils.changeStatusColor(this, R.color.color_050D4C)
        Utils.changeStatusTextColor(this)

        //binding.toolbarChat.ivBack.setOnClickListener { finish() }
        binding.toolbarChat.toolbarTitle.text = "John doe"
        binding.toolbarChat.ivBack.visibility = INVISIBLE
        binding.toolbarChat.ivUserImage.visibility = VISIBLE
        binding.toolbarChat.cutmToolBarRightIcon2.setImageResource(R.drawable.ic_call_white_top17)
        binding.toolbarChat.cutmToolBarRightIcon3.visibility = VISIBLE

        binding.toolbarChat.cutmToolBarRightIcon.setOnClickListener {
            showPopMenu(it)
           // heloo()
        }

        binding.toolbarChat.deliveryCount.setOnClickListener {
            showPOPUP()
        }

    }

    private fun showPOPUP() {
        PopUpVehicleChoose.getInstance().createDialog(
            this@ChatActivity,
            "",
            object : PopUpVehicleChoose.Dialogclick {
                override fun onYes() {
                    riderLoginChk()
                }

                override fun onNo() {

                }

                override fun onPedesstrain() {

                }

                override fun onCycle() {

                }

                override fun onElectric() {

                }

                override fun onPetrol() {

                }

                override fun onBio() {

                }

            })
    }

    private fun riderLoginChk() {
        // startActivity(Intent(this@UserHomePageNavigationActivity, HomeRiderrActivity::class.java))

        if (PreferenceKeeper.instance.loginResponse != null) {
            if (PreferenceKeeper.instance.loginResponse!!.isRider) {
                // MyApp.popErrorMsg("","Your Vehicle details is in under verification",THIS!!)
                startActivity(
                    Intent(
                        this@ChatActivity,
                        HomeRiderrActivity::class.java
                    )
                )
                finish()
            } else {
                startActivity(
                    Intent(
                        this@ChatActivity,
                        VehicleInfoActivity::class.java
                    )
                )
            }
        } else {
            goLoginScreen()
        }
    }
    private fun goLoginScreen() {
        Utils.showMessage(this, resources.getString(R.string.you_login_first))
        startActivity(Intent(this@ChatActivity, LoginActivity::class.java))
        finish()
    }

    private fun showPopMenu(viewe: View?) {
        val balloon = Balloon.Builder(this@ChatActivity)
            .setWidthRatio(0.4f)
            .setLayout(R.layout.menu_popup_chat)
            .setWidth(BalloonSizeSpec.WRAP)
            .setHeight(BalloonSizeSpec.WRAP)
            .setTextColorResource(R.color.color_333333)
            .setTextSize(15f)
            .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
            .setArrowSize(10)
            .setArrowPosition(0.5f)
            .setPadding(8)
            .setCornerRadius(5f)
            .setBackgroundColorResource(R.color.white)
            .setBalloonAnimation(BalloonAnimation.CIRCULAR)
            .build()
        if(balloon.isShowing){
            balloon.dismiss()
        }else {
            balloon.showAsDropDown(viewe!!)
        }
        val menuViewContact: TextView = balloon.getContentView().findViewById(R.id.menuViewContact)
        val changeWallpaper: TextView = balloon.getContentView().findViewById(R.id.changeWallpaper)
        menuViewContact.setOnClickListener {
            Toast.makeText(this@ChatActivity, "ViewContact", Toast.LENGTH_SHORT).show()
            balloon.dismiss()
        }

        changeWallpaper.setOnClickListener {
            startActivity(Intent(this,WallpaperActivity::class.java))
            balloon.dismiss()
        }

    }
}