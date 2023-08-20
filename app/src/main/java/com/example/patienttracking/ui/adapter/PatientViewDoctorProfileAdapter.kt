package com.example.patienttracking.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.patienttracking.R
import com.example.patienttracking.models.DoctorInfo

class PatientViewDoctorProfileAdapter(private val doctorList : ArrayList<DoctorInfo>) :
    RecyclerView.Adapter<PatientViewDoctorProfileAdapter.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.doctor_profile_info,parent,false)
        return PatientViewDoctorProfileAdapter.MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = doctorList[position]

        holder.dName.text = currentItem.name
        holder.dDegree.text = currentItem.degree
        holder.dSpecialization.text = currentItem.specialty
    }

    override fun getItemCount(): Int {
      return  doctorList.size
    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val dName = itemView.findViewById<TextView>(R.id.setDocNameP)
        val dDegree = itemView.findViewById<TextView>(R.id.setDDegreeP)
        val dSpecialization = itemView.findViewById<TextView>(R.id.setDSpecialP)

    }
}