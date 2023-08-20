package com.example.patienttracking.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.patienttracking.R
import com.example.patienttracking.models.User

class AdminProfileInfoAdapter (private val userList : ArrayList<User>) : RecyclerView.Adapter<AdminProfileInfoAdapter.MyViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.admin_user_info_view,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]
        holder.userInfoName.text = currentItem.name
        holder.userInfoEmail.text = currentItem.email
        holder.userInfoPhoneNumber.text = currentItem.phoneNumber

    }
    override fun getItemCount(): Int {
        return userList.size
    }
    class MyViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){

        val userInfoName = itemView.findViewById<TextView>(R.id.userNameAdmin)
        val userInfoEmail = itemView.findViewById<TextView>(R.id.userEmailAdmin)
        val userInfoPhoneNumber = itemView.findViewById<TextView>(R.id.userPhoneNumberAdmin)

    }
}