package com.example.patienttracking

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.patienttracking.models.DoctorInfo
import com.example.patienttracking.models.User
import com.example.patienttracking.ui.DoctorMainPageFragment
import com.example.patienttracking.ui.LoadingDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class DoctorProfileViewByPatientFragment : Fragment() {
    private var firebaseDatabase : FirebaseDatabase? = null
    private var databaseReference : DatabaseReference? = null
    private lateinit var doctorInfo : DoctorInfo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.reference
        // Inflate the layout for this fragment
       val view =  inflater.inflate(R.layout.fragment_doctor_profile_view_by_patient, container, false)
        val updateP = view.findViewById<TextView>(R.id.setValueUpdate)
        val updateName = view.findViewById<TextView>(R.id.setValueName)
        val updateDegree = view.findViewById<TextView>(R.id.setValueDegree)
        val updateSpeciality = view.findViewById<TextView>(R.id.setValueSpecialty)
        val buttonB = view.findViewById<Button>(R.id.backButtonPhone)
        buttonB.setOnClickListener {
            val viewPage = DoctorProfileFragment()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.frameLayout, viewPage)
            transaction.commit()
        }
        updateP.setOnClickListener {
            val dUpdate = DoctorUpdateProfileFragment()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.frameLayout, dUpdate)
            transaction.commit()
        }



        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
        currentUserEmail?.let { email ->
            databaseReference?.child("DoctorDetails")?.orderByChild("email")?.equalTo(email)
                ?.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            // Here, snapshot refers to the user with the specified email
                            for (userSnapshot in snapshot.children) {
                                doctorInfo = userSnapshot.getValue(DoctorInfo::class.java)!!
                                updateName.text = doctorInfo.name
                                updateDegree.text = doctorInfo.degree
                                updateSpeciality.text = doctorInfo.specialty
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




        return view
    }


}