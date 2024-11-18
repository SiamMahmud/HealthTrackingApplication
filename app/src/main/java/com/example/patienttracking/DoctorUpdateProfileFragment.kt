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
import com.example.patienttracking.utils.FirebaseUtil.firebaseAuth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class DoctorUpdateProfileFragment : Fragment() {
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
        val view =  inflater.inflate(R.layout.fragment_doctor_update_profile, container, false)
         var bButton = view.findViewById<TextView>(R.id.doctorMainPageButton)
        var sentN = view.findViewById<EditText>(R.id.dNa)
        var sentD = view.findViewById<EditText>(R.id.dDegree)
        var sentS = view.findViewById<EditText>(R.id.dSpe)
        var sentE = view.findViewById<EditText>(R.id.dEm)
        var sentData = view.findViewById<Button>(R.id.dSentS)


        bButton.setOnClickListener {
            val dProfile = DoctorProfileFragment()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.frameLayout, dProfile)
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
                                sentN.text = Editable.Factory.getInstance().newEditable(doctorInfo.name)
                                sentE.text = Editable.Factory.getInstance().newEditable(doctorInfo.email)
                                sentD.text = Editable.Factory.getInstance().newEditable(doctorInfo.degree)
                                sentS.text =  Editable.Factory.getInstance().newEditable(doctorInfo.specialty)
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


        databaseReference = FirebaseDatabase.getInstance().reference.child("DoctorDetails")
        sentData.setOnClickListener {
            val name = sentN.text.toString()
            val email = sentE.text.toString()
            val degree = sentD.text.toString()
            val specialty = sentS.text.toString()
            val userEmail = firebaseAuth.currentUser?.email

            if (userEmail != null) {
                val doctor = DoctorInfo(name, email, degree, specialty)
                val newDoctorRef = databaseReference!!.child(userEmail.replace(".", ",")) // Replace "." with "," to avoid Firebase key restrictions
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