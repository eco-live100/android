package com.app.ecolive.msg_module

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.ecolive.R
import com.app.ecolive.databinding.ActivityUserPaymentCheckoutBinding
import com.app.ecolive.databinding.ChatlistActivityBinding
import com.app.ecolive.localmodel.ChatListModel
import com.app.ecolive.login_module.LoginActivity
import com.app.ecolive.msg_module.adapter.ChatListAdapter
import com.app.ecolive.rider_module.HomeRiderrActivity
import com.app.ecolive.rider_module.VehicleInfoActivity
import com.app.ecolive.user_module.FullImageActivity
import com.app.ecolive.utils.PopUpVehicleChoose
import com.app.ecolive.utils.PreferenceKeeper
import com.app.ecolive.utils.Utils

class ChatListActivity : AppCompatActivity() {

    lateinit var binding: ChatlistActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this@ChatListActivity,R.layout.chatlist_activity)
        setToolBar()
        setListDummy()

        binding.chatCreateGrp.setOnClickListener {
            startActivity(Intent(this@ChatListActivity,CreateGrpActivity::class.java))
        }

        binding.toolbarChatList.deliveryCount.setOnClickListener{
            showPOPUP()
        }

    }

    lateinit var chatListAdapter: ChatListAdapter

    private fun setListDummy() {
        var list =ArrayList<ChatListModel>()
        list.add(ChatListModel(R.drawable.dummy_female_user,"Gail Forcewind","Good Work Environment and Culture...","15 Min ago","2",true,true))
        list.add(ChatListModel(R.drawable.dummy_male_user,"Cameron Williamson","Good Work Environment and Culture...","4:50 PM","2",true,false))
        list.add(ChatListModel(R.drawable.dummy_male_user,"Leslie Alexander","Good Work Environment and Culture...","11:50 PM","2", isVideo = false, isRead = true))
        list.add(ChatListModel(R.drawable.dummy_female_user,"Gail Forcewind","Good Work Environment and Culture...","15 Min ago","2",true,true))
        list.add(ChatListModel(R.drawable.dummy_male_user,"Cameron Williamson","Good Work Environment and Culture...","4:50 PM","2",true,false))
        list.add(ChatListModel(R.drawable.dummy_male_user,"Leslie Alexander","Good Work Environment and Culture...","11:50 PM","2", isVideo = false, isRead = true))
        list.add(ChatListModel(R.drawable.dummy_female_user,"Gail Forcewind","Good Work Environment and Culture...","15 Min ago","2",true,true))
        list.add(ChatListModel(R.drawable.dummy_male_user,"Cameron Williamson","Good Work Environment and Culture...","4:50 PM","2",true,false))
        list.add(ChatListModel(R.drawable.dummy_male_user,"Leslie Alexander","Good Work Environment and Culture...","11:50 PM","2", isVideo = false, isRead = true))
        chatListAdapter = ChatListAdapter(this@ChatListActivity,list,object:ChatListAdapter.ClickListener{
            override fun onClick(pos: Int) {
                    startActivity(Intent(this@ChatListActivity,ChatActivity::class.java))
            }

            override fun onClickImg(pos: Int) {
                startActivity(Intent(this@ChatListActivity,FullImageActivity::class.java))
            }

        })

        binding.recycleChatList.also {
            it.layoutManager = LinearLayoutManager(this@ChatListActivity,LinearLayoutManager.VERTICAL,false)
            it.adapter = chatListAdapter
        }

    }

    private fun setToolBar() {
        Utils.changeStatusColor(this, R.color.color_050D4C)
        Utils.changeStatusTextColor(this)

        binding.toolbarChatList.toolbarTitle.text="Messaging"
        binding.toolbarChatList.cutmToolBarRightIcon2.visibility=View.INVISIBLE
        binding.toolbarChatList.ivUserImage.visibility=View.GONE
        binding.toolbarChatList.ivBack.visibility=View.VISIBLE
        binding.toolbarChatList.ivBack.setOnClickListener { finish() }
    }

    private fun showPOPUP() {
        PopUpVehicleChoose.getInstance().createDialog(
            this@ChatListActivity,
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
                        this@ChatListActivity,
                        HomeRiderrActivity::class.java
                    )
                )
                finish()
            } else {
                startActivity(
                    Intent(
                        this@ChatListActivity,
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
        startActivity(Intent(this@ChatListActivity, LoginActivity::class.java))
        finish()
    }
}