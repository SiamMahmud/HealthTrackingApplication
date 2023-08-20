package com.example.patienttracking.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentTransaction
import com.example.patienttracking.R

class DoctorWritePrescriptionFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
       val view =  inflater.inflate(R.layout.fragment_doctor_write_prescription, container, false)
        val backMainDoctorBtn = view.findViewById<Button>(R.id.doctorPrescriptionHomeBtn)
        val findPatientButton = view.findViewById<Button>(R.id.findPatientBtn)
        val cardViewPatientDetails = view.findViewById<CardView>(R.id.findPatientDetailsTab)
        backMainDoctorBtn.setOnClickListener {
            val doctorMainPage = DoctorMainPageFragment()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.frameLayout,doctorMainPage)
            transaction.commit()
        }
        findPatientButton.setOnClickListener {
            findPatientButton.visibility = View.GONE
            cardViewPatientDetails.visibility = View.VISIBLE


        }

        return  view
    }

}