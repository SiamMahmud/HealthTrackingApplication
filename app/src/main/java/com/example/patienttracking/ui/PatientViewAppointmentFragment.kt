package com.example.patienttracking.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentTransaction
import com.example.patienttracking.R
import com.example.patienttracking.models.Appointment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class PatientViewAppointmentFragment : Fragment() {
    private var firebaseDatabase : FirebaseDatabase? = null
    private var databaseReference : DatabaseReference? = null
    private lateinit var appointment: Appointment


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.reference
        // Inflate the layout for this fragment
       val view = inflater.inflate(R.layout.fragment_patient_view_appointment, container, false)
        val dShow = view.findViewById<TextView>(R.id.showBookingDate)
        val nShow = view.findViewById<TextView>(R.id.showDoctorName)
        val eShow = view.findViewById<TextView>(R.id.showDoctorEmail)
        val viewDShow = view.findViewById<Button>(R.id.viewBookingDateButton)
        val viewDate = view.findViewById<CardView>(R.id.viewDate)
        viewDShow.setOnClickListener {
            val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
            currentUserEmail?.let { email ->
                databaseReference?.child("Appointments")?.orderByChild("userEmail")?.equalTo(email)
                    ?.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                for (userSnapshot in snapshot.children) {
                                    userSnapshot.getValue(Appointment::class.java)!!
                                        .also { appointment = it }
                                    dShow.text = appointment.appointmentDate
                                    nShow.text = appointment.doctorName
                                    eShow.text = appointment.doctorEmail
                                }
                            } else {
                                Toast.makeText(activity, "Data not found", Toast.LENGTH_LONG).show()
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Log.e("ViewProfileFragment", "Data retrieval cancelled: ${databaseError.message}")
                        }
                    })
            }
            viewDate.visibility = View.VISIBLE

            viewDShow.visibility = View.GONE

        }

        val backHomePageBtn = view.findViewById<Button>(R.id.patientViewAppointmentHomeBackBtn)
        backHomePageBtn.setOnClickListener {
            val patientMainPage = PatientMainPageFragment()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.frameLayout, patientMainPage)
            transaction.commit()
        }


        return view
    }

}
