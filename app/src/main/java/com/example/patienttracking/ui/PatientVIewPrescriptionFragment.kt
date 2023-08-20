package com.example.patienttracking.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentTransaction
import com.example.patienttracking.R


class PatientVIewPrescriptionFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
       val v =  inflater.inflate(R.layout.fragment_patient_view_prescription, container, false)
        val backButtonPrescription = v.findViewById<Button>(R.id.patientHomeBtnPrescription)
        backButtonPrescription.setOnClickListener {
            val patientMPage = PatientMainPageFragment()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.frameLayout,patientMPage)
            transaction.commit()
        }
        return  v

    }

}