package com.example.patienttracking.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentTransaction
import com.example.patienttracking.DoctorProfileFragment
import com.example.patienttracking.R


class DoctorMainPageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_doctor_main_page, container, false)
        val backButton = view.findViewById<Button>(R.id.backButtonDoctor)
        val viewPaDetails = view.findViewById<Button>(R.id.doctorViewPatientDetailsBtn)
        val writePrescriptionBtn = view.findViewById<Button>(R.id.writePrescriptionBtn )
        val viewP = view.findViewById<Button>(R.id.doctorOwnProfile)
        var builder : AlertDialog.Builder
        var con = this.context
        con?.let {
            builder = AlertDialog.Builder(con)
        }
        backButton.setOnClickListener {
            con?.let {
                builder = AlertDialog.Builder(con)
                builder.setTitle("Alert!")

                    .setMessage("Do you want to log Out?")
                    .setCancelable(true)
                    .setPositiveButton("Yes"){dialogInterface,it->
                        val loginPage = LoginFragment()
                        val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
                        transaction.replace(R.id.frameLayout,loginPage)
                        transaction.commit()
                    }
                    .setNegativeButton("No"){dialogInterface,it->
                        dialogInterface.cancel()
                    }
                    .show()
            }

        }
        viewPaDetails.setOnClickListener {
            
        }
        writePrescriptionBtn.setOnClickListener {
            val writeP = DoctorWritePrescriptionFragment()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.frameLayout,writeP)
            transaction.commit()
        }

        viewP.setOnClickListener {
            val profilePage =DoctorProfileFragment()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.frameLayout, profilePage)
            transaction.commit()
        }



        return  view
    }
}