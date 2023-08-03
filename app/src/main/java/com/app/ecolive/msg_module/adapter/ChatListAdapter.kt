package com.app.ecolive.msg_module.adapter
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.ecolive.R
import com.app.ecolive.databinding.*
import com.app.ecolive.localmodel.ChatListModel
import com.app.ecolive.localmodel.SimilarProductListModel
import com.app.ecolive.localmodel.TransactionHistoryListModel
import com.localmerchants.ui.localModels.DrawerCategoryListModel


class ChatListAdapter(var context: Context, var dataList: ArrayList<ChatListModel>, var clickListern:ClickListener) :
    RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView : RowChatlistBinding)
        : RecyclerView.ViewHolder(itemView.root){
        var  binding : RowChatlistBinding = itemView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RowChatlistBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_chatlist, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.chatTitle.text=dataList[position].title
        holder.binding.chatSubTitle.text=dataList[position].subTitle
        holder.binding.chatTimeAgo.text=dataList[position].time
        holder.binding.chatMsgCount.text=dataList[position].msgCount
        holder.binding.chatImage.setImageResource(dataList[position].img)
        if(dataList[position].isVideo){
            holder.binding.chatSubTitle.setText("Video")
            holder.binding.chatSubTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_videocam,0,0,0)
        }else{
            holder.binding.chatSubTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0)
        }
        if(dataList[position].isRead){
           holder.binding.chatTicIcon.setImageResource(R.drawable.ic_ble_double_tic)
        }else{
            holder.binding.chatTicIcon.setImageResource(R.drawable.ic_gray_double_tic)
        }

        holder.itemView.setOnClickListener {
            clickListern.onClick(position)
        }

        holder.binding.chatImage.setOnClickListener {
            clickListern.onClickImg(position)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    interface ClickListener {
        fun onClick(pos: Int)
        fun onClickImg(pos: Int)
    }
}

