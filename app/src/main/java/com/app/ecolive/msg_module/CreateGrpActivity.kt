package com.app.ecolive.msg_module

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.ecolive.R
import com.app.ecolive.databinding.ActivityUserPaymentCheckoutBinding
import com.app.ecolive.databinding.ChatlistActivityBinding
import com.app.ecolive.databinding.CreateGrpActivityBinding
import com.app.ecolive.localmodel.ChatListModel
import com.app.ecolive.msg_module.adapter.ChatListAdapter
import com.app.ecolive.msg_module.adapter.CreateGrpListAdapter
import com.app.ecolive.utils.Utils

class CreateGrpActivity : AppCompatActivity() {

    lateinit var binding: CreateGrpActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this@CreateGrpActivity,R.layout.create_grp_activity)
        setToolBar()
        setListDummy()
    }

    lateinit var chatListAdapter: CreateGrpListAdapter
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
        list.add(ChatListModel(R.drawable.dummy_female_user,"Gail Forcewind","Good Work Environment and Culture...","15 Min ago","2",true,true))
        list.add(ChatListModel(R.drawable.dummy_male_user,"Cameron Williamson","Good Work Environment and Culture...","4:50 PM","2",true,false))
        list.add(ChatListModel(R.drawable.dummy_male_user,"Leslie Alexander","Good Work Environment and Culture...","11:50 PM","2", isVideo = false, isRead = true))
        chatListAdapter = CreateGrpListAdapter(this@CreateGrpActivity,list,object:CreateGrpListAdapter.ClickListener{
            override fun onClick(pos: Int) {

            }

        })

        binding.recycleCreateGrp.also {
            it.layoutManager = LinearLayoutManager(this@CreateGrpActivity,LinearLayoutManager.VERTICAL,false)
            it.adapter = chatListAdapter
        }

    }

    private fun setToolBar() {
        Utils.changeStatusColor(this, R.color.color_050D4C)
        Utils.changeStatusTextColor(this)

        binding.toolbarChatList.toolbarTitle.text="Messaging"
        binding.toolbarChatList.ivBack.setOnClickListener { finish() }
    }
}