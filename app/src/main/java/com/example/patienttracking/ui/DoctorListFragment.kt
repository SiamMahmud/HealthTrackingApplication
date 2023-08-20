package com.example.patienttracking.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.patienttracking.R
import com.example.patienttracking.models.User
import com.example.patienttracking.ui.adapter.AdminProfileInfoAdapter
import com.google.firebase.database.*

class DoctorListFragment : Fragment() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<User>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_doctor_list, container, false)
        val bButton = view.findViewById<Button>(R.id.backButtonBookAppointmentPage)
        bButton.setOnClickListener {
            val pBookFragment = PatientBookAppointmentFragment()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.frameLayout,pBookFragment)
            transaction.commit()
        }
        userRecyclerView = view.findViewById(R.id.doctorUserList)
        userRecyclerView.layoutManager= LinearLayoutManager(this.context)
        userRecyclerView.setHasFixedSize(true)
        userArrayList = arrayListOf<User>()
        getDoctorData()
        return view
    }
    private fun getDoctorData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Doctor")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    userArrayList.clear()
                    for (userSnapShot in snapshot.children) {
                        try {
                            val user = userSnapShot.getValue(User::class.java)
                            user?.let { userArrayList.add(it) }
                        } catch (e: Exception) {
                            Log.e("TAG", "Error while deserializing user data: ${e.message}")
                        }
                    }
                    userRecyclerView.adapter = AdminProfileInfoAdapter(userArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error if needed
            }
        })
    }


}