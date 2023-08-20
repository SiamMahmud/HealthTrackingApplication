package com.example.patienttracking

import android.os.Bundle
import android.sax.EndElementListener
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.patienttracking.models.DoctorInfo
import com.example.patienttracking.models.User
import com.example.patienttracking.ui.DoctorMainPageFragment
import com.example.patienttracking.ui.LoginFragment
import com.example.patienttracking.utils.FirebaseUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class DoctorUpdateProfileFragment : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database : DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_doctor_update_profile, container, false)
         var bButton = view.findViewById<TextView>(R.id.doctorMainPageButton)
        var sentN = view.findViewById<EditText>(R.id.dNa)
        var sentD = view.findViewById<EditText>(R.id.dDegree)
        var sentS = view.findViewById<EditText>(R.id.dSpe)
        var sentE = view.findViewById<EditText>(R.id.dEm)
        var sentData = view.findViewById<Button>(R.id.dSentS)
        firebaseAuth = FirebaseAuth.getInstance()

        bButton.setOnClickListener {
            val dProfile = DoctorProfileFragment()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.frameLayout, dProfile)
            transaction.commit()
        }
            database = FirebaseDatabase.getInstance().reference.child("DoctorDetails")

        sentData.setOnClickListener {
            val name = sentN.text.toString()
            val email = sentE.text.toString()
            val degree = sentD.text.toString()
            val specialty = sentS.text.toString()
            val userEmail = firebaseAuth.currentUser?.email

            if (userEmail != null) {
                val doctor = DoctorInfo(name, email, degree, specialty)
                val newDoctorRef = database.child(userEmail.replace(".", ",")) // Replace "." with "," to avoid Firebase key restrictions
                newDoctorRef.setValue(doctor)
                Toast.makeText(requireContext(), "Doctor data sent successfully", Toast.LENGTH_SHORT).show()
            } else {
                // Handle case where user is not logged in or email is not available
                Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            }
            sentN.text.clear()
            sentE.text.clear()
            sentD.text.clear()
            sentS.text.clear()
        }



        return view
    }

}