package com.example.patienttracking.ui

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentTransaction
import com.example.patienttracking.R
import com.google.firebase.database.FirebaseDatabase


class AdminCreateAppointmentFragment : Fragment() {


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_admin_create_appointment, container, false)
        val bacKAdminMainPgBtn = view.findViewById<Button>(R.id.adminCancelAppointmentHomeBackBtn)
        val setAvailableDateBtn = view.findViewById<Button>(R.id.adminSetAvailableDateBtn)
        setAvailableDateBtn.setOnClickListener {
            // Open a dialog or fragment to select the available date
            val datePickerDialog = DatePickerDialog(requireContext())
            datePickerDialog.setOnDateSetListener { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)

                // Save the selected date to Firebase
                val firebaseDatabase = FirebaseDatabase.getInstance()
                val availableDatesRef = firebaseDatabase.reference.child("AvailableAppointmentDates")
                availableDatesRef.child(selectedDate.timeInMillis.toString()).setValue(true)
            }
            datePickerDialog.show()
        }
        bacKAdminMainPgBtn.setOnClickListener {
            val pMainPage = AdminMainPageFragment()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.frameLayout, pMainPage)
            transaction.commit()
        }

        return  view
    }

}