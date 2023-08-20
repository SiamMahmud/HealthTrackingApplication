package com.example.patienttracking.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import com.example.patienttracking.Communicator
import com.example.patienttracking.R

class MainActivity : AppCompatActivity(), Communicator {
    lateinit var binding: AppCompatActivity
    val fragmentManager = supportFragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (findViewById<FrameLayout>(R.id.frameLayout) != null) {
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameLayout, LoginFragment())
            fragmentTransaction.commit()
        }

    }

    override fun passDataCom(textViewInput: String) {
        val bundle = Bundle()
        bundle.putString("message", textViewInput)

        val viewAppoint = PatientViewAppointmentFragment()
        viewAppoint.arguments = bundle
    }
}