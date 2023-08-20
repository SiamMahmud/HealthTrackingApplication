package com.example.patienttracking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.patienttracking.models.DoctorInfo
import com.example.patienttracking.models.User
import com.example.patienttracking.ui.AdminMainPageFragment
import com.example.patienttracking.ui.PatientBookAppointmentFragment
import com.example.patienttracking.ui.adapter.AdminProfileInfoAdapter
import com.example.patienttracking.ui.adapter.PatientViewDoctorProfileAdapter
import com.google.firebase.database.*


class PatientViewDoctorProfileFragment : Fragment() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var doctorRecyclerView: RecyclerView
    private lateinit var doctorArrayList: ArrayList<DoctorInfo>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_patient_view_doctor_profile, container, false)
        val backBtn = view.findViewById<Button>(R.id.backButtonBookingPage)
        backBtn.setOnClickListener {
            val bookingPage = PatientBookAppointmentFragment()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.frameLayout,bookingPage)
            transaction.commit()
        }


        doctorRecyclerView = view.findViewById(R.id.doctorRecycleView)
        doctorRecyclerView.layoutManager= LinearLayoutManager(this.context)
        doctorRecyclerView.setHasFixedSize(true)
        doctorArrayList = arrayListOf<DoctorInfo>()
        getUserdata()


        return view
    }

    private fun getUserdata() {
        databaseReference = FirebaseDatabase.getInstance().getReference("DoctorDetails")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (doctorSnapShot in snapshot.children) {
                        val doctor = doctorSnapShot.getValue(DoctorInfo::class.java)
                        if (doctor != null) {
                            doctorArrayList.add(doctor)
                        }
                    }
                    doctorRecyclerView.adapter = PatientViewDoctorProfileAdapter(doctorArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the onCancelled situation here if needed
            }
        })

    }

}