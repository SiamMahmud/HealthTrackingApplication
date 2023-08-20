package com.example.patienttracking.ui

import android.os.Bundle
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

class AdminViewPatientDetailsFragment : Fragment() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<User>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_admin_view_patient_details, container, false)
        val backBt = view.findViewById<Button>(R.id.backButtonAdminMainPage)
        backBt.setOnClickListener {
            val adminMainPage = AdminMainPageFragment()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.frameLayout,adminMainPage)
            transaction.commit()

        }

        userRecyclerView = view.findViewById(R.id.userList)
        userRecyclerView.layoutManager= LinearLayoutManager(this.context)
        userRecyclerView.setHasFixedSize(true)
        userArrayList = arrayListOf<User>()
        getUserdata()



        return view


    }

    private fun getUserdata() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot .exists()){
                    for (userSnapShot in snapshot.children){
                        val user = userSnapShot.getValue(User::class.java)
                        userArrayList.add(user!!)
                    }
                    userRecyclerView.adapter = AdminProfileInfoAdapter(userArrayList)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

}