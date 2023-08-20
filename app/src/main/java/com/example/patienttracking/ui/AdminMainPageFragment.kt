package com.example.patienttracking.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentTransaction
import com.example.patienttracking.R

class AdminMainPageFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_admin_main_page, container, false)
        val btn = view.findViewById<Button>(R.id.blogInBtnAdmin)
        val viewPDBtn = view.findViewById<Button>(R.id.viewPDetailsBtn)
        val removeDeBtn= view.findViewById<Button>(R.id.removePatientDetailsBtn)
        val createAppointBtn = view.findViewById<Button>(R.id.createAppointmentBtn)
        val cancelAppointBtn = view.findViewById<Button>(R.id.cancelAppointmentBtn)
        var builder : AlertDialog.Builder
        var con = this.context
        con?.let {
            builder = AlertDialog.Builder(con)
        }
        viewPDBtn.setOnClickListener {
            val profileInfo = AdminViewPatientDetailsFragment()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.frameLayout,profileInfo)
            transaction.commit()
        }
        removeDeBtn.setOnClickListener {
            val deleteInfoFragment = AdminRemovePatientDetailsFragment()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.frameLayout,deleteInfoFragment)
            transaction.commit()
        }
        createAppointBtn.setOnClickListener {
            val createAppointFragment = AdminCreateAppointmentFragment()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.frameLayout,createAppointFragment)
            transaction.commit()
        }

        btn.setOnClickListener {
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


        return view
    }

}