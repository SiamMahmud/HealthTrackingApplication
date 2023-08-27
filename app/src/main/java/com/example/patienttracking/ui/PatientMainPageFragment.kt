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

class PatientMainPageFragment : Fragment() {

    private lateinit var builder: AlertDialog.Builder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_patient_main_page, container, false)
        val bookAppointBtn = view.findViewById<Button>(R.id.bookAppointment)
        val viewAppointBtn = view.findViewById<Button>(R.id.viewAppointment)
        val viewPrescripBtn = view.findViewById<Button>(R.id.viewPrescription)
        val viewDetails = view.findViewById<Button>(R.id.viewOwnDetails)
        val logoutBtn = view.findViewById<Button>(R.id.logOutBtn)
        var builder : AlertDialog.Builder
        var con = this.context
        con?.let {
            builder = AlertDialog.Builder(con)
        }
        bookAppointBtn.setOnClickListener {
            val patientBookAppointPage = PatientBookAppointmentFragment()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.frameLayout,patientBookAppointPage)
            transaction.commit()
        }
        viewAppointBtn.setOnClickListener {
            val patientViewAppointPage = PatientViewAppointmentFragment()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.frameLayout,patientViewAppointPage)
            transaction.commit()
        }
        viewPrescripBtn.setOnClickListener {
            val patientViewPrescripPage = PatientVIewPrescriptionFragment()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.frameLayout,patientViewPrescripPage)
            transaction.commit()
        }
        viewDetails.setOnClickListener {
            
            val patientViewDetailsPage = ViewProfileFragment()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.frameLayout,patientViewDetailsPage)
            transaction.commit()


        }
        logoutBtn.setOnClickListener {
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