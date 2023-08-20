package com.example.patienttracking

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.patienttracking.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DoctorEditProfileFragment : Fragment() {




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_doctor_edit_profile, container, false)
        var updateDoctorN = view.findViewById<EditText>(R.id.updateDoctorName)
        var updateDoctorE = view.findViewById<EditText>(R.id.updateDoctorEmail)
        var updateDoctorP = view.findViewById<EditText>(R.id.updateDoctorPhoneNumber)
        var b = view.findViewById<TextView>(R.id.pButtonB)




        return view
    }

}