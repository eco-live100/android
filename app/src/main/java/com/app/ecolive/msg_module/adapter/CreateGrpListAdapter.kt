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


class CreateGrpListAdapter(var context: Context, var dataList: ArrayList<ChatListModel>, var clickListern:ClickListener) :
    RecyclerView.Adapter<CreateGrpListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView : RowCreategrpBinding)
        : RecyclerView.ViewHolder(itemView.root){
        var  binding : RowCreategrpBinding = itemView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RowCreategrpBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_creategrp, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.chatTitle.text=dataList[position].title

        holder.binding.chatImage.setImageResource(dataList[position].img)
//        if(dataList[position].isVideo){
//
//        }else{
//
//        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    interface ClickListener {
        fun onClick(pos: Int)
    }
}

