package com.example.patienttracking.ui



import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.FragmentTransaction
import com.example.patienttracking.Communicator
import com.example.patienttracking.PatientViewDoctorProfileFragment
import com.example.patienttracking.R
import com.example.patienttracking.models.Appointment
import com.example.patienttracking.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class PatientBookAppointmentFragment : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var setDName: TextView
    private lateinit var setDEmail: TextView
    private lateinit var sDoctor : EditText
    private lateinit var DoctorD : CardView

    private lateinit var communicator: Communicator
    private val availableDatesSet = HashSet<String>()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val con = this.context
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_patient_book_appointment, container, false)
       communicator = activity as Communicator
        val bookAppointBackHomeBtn = view.findViewById<Button>(R.id.patientHomePageBackBtn)
        val dateButton = view.findViewById<Button>(R.id.dateButton)
        val message = view.findViewById<TextView>(R.id.dateText)
        val bookConfirmationBtn = view.findViewById<Button>(R.id.bookConfirmBtn)
        val dList = view.findViewById<Button>(R.id.DoctorList)
        setDName = view.findViewById(R.id.setDoctorName)
        setDEmail = view.findViewById(R.id.setDoctorEmail)
        sDoctor = view.findViewById(R.id.searchDoctorE)
        DoctorD = view.findViewById(R.id.detailsD)
        val findDetailsBtn = view.findViewById<Button>(R.id.findDoctorDetailsButton)
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        fetchAvailableDates()
        dateButton.setOnClickListener {
            con?.let {
                val dpd = DatePickerDialog(
                    con, DatePickerDialog.OnDateSetListener { _, myear, mmonth, mday ->
                        val selectedDate = Calendar.getInstance()
                        selectedDate.set(myear, mmonth, mday)

                        val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                        val formattedDate = sdf.format(selectedDate.time)

                        if (availableDatesSet.contains(formattedDate)) {
                            message.text = formattedDate
                        } else {
                            Toast.makeText(
                                activity,
                                "Date is not available",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }, year, month, day
                )
                val maxDate = c.clone() as Calendar
                maxDate.add(Calendar.DAY_OF_MONTH, 30) // Adjust this value as needed

                dpd.datePicker.minDate = c.timeInMillis
                dpd.datePicker.maxDate = maxDate.timeInMillis

                val fragmentContext = requireContext()

                dpd.setOnShowListener {
                    val datePicker: DatePicker = dpd.datePicker
                    for (i in 0 until datePicker.childCount) {
                        val child = datePicker.getChildAt(i)
                        if (child is ViewGroup) {
                            for (j in 0 until child.childCount) {
                                val cell = child.getChildAt(j)
                                if (cell is TextView) {
                                    val cellDate = Calendar.getInstance()
                                    cellDate.set(year, month, cell.text.toString().toInt())

                                    val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                                    val formattedCellDate = sdf.format(cellDate.time)

                                    if (availableDatesSet.contains(formattedCellDate)) {
                                        cell.setTextColor(ContextCompat.getColor(fragmentContext, R.color.availableDateColor))
                                    } else {
                                        cell.setTextColor(ContextCompat.getColor(fragmentContext, R.color.unavailableDateColor))
                                    }
                                }
                            }
                        }
                    }
                }

                dpd.show()
            }
        }

        bookConfirmationBtn.setOnClickListener {
            val userEmail = FirebaseAuth.getInstance().currentUser?.email
            val appointmentDate = message.text.toString()
            val doctorName = setDName.text.toString()
            val doctorEmail = setDEmail.text.toString()
            if (userEmail != null && userEmail.isNotEmpty() && appointmentDate.isNotEmpty() &&
                doctorName.isNotEmpty() && doctorEmail.isNotEmpty()
            ) {

                val appointment = Appointment(userEmail, appointmentDate, doctorName, doctorEmail)

                saveAppointmentToDatabase(appointment)
                Toast.makeText(activity, "Booking Confirmed", Toast.LENGTH_LONG).show()
                DoctorD.visibility = View.GONE
            } else {
                Toast.makeText(activity, "Please fill all fields", Toast.LENGTH_LONG).show()
            }
        }
        findDetailsBtn.setOnClickListener {
            val userEmail: String = sDoctor.text.toString().trim()
            if (userEmail.isNotEmpty()) {
                readData(userEmail)
            } else {
                Toast.makeText(activity, "Please enter the email address", Toast.LENGTH_LONG).show()
            }
        }

        dList.setOnClickListener {
            val dhList = PatientViewDoctorProfileFragment()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.frameLayout,dhList)
            transaction.commit()
        }
        bookAppointBackHomeBtn.setOnClickListener {
            val patientMainPage = PatientMainPageFragment()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.frameLayout,patientMainPage)
            transaction.commit()}
        return view
    }

    private fun fetchAvailableDates() {
        val database = FirebaseDatabase.getInstance().getReference("AvailableAppointmentDates")
        database.addValueEventListener(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                availableDatesSet.clear()

                for (dateSnapshot in dataSnapshot.children) {
                    val timestamp = dateSnapshot.key
                    val date = timestamp?.toLong()?.let {
                        SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                            .format(Date(it))
                    }
                    date?.let { availableDatesSet.add(it) }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if the data fetching fails
            }
        })
    }

    private fun saveAppointmentToDatabase(appointment: Appointment) {
        val database = FirebaseDatabase.getInstance().getReference("Appointments")
        val userEmail = FirebaseAuth.getInstance().currentUser?.email
        val userName = userEmail?.substringBefore("@")

        if (userName != null) {
            database.child(userName).setValue(appointment)
                .addOnSuccessListener {
                    Toast.makeText(activity, "Appointment saved successfully", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(activity, "Failed to save appointment", Toast.LENGTH_LONG).show()
                }
        } else {
            Toast.makeText(activity, "Failed to get the username", Toast.LENGTH_LONG).show()
        }
    }

    private fun readData(email: String) {
        database = FirebaseDatabase.getInstance().getReference("Users")
        val query = database.orderByChild("email").equalTo(email)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var userFound = false

                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    setDName.text = user?.name
                    setDEmail.text = user?.email
                    DoctorD.visibility = View.VISIBLE

                    Toast.makeText(activity, "Found the data successfully", Toast.LENGTH_LONG).show()
                    userFound = true
                    break
                }

                if (!userFound) {
                    Toast.makeText(activity, "User Doesn't exist", Toast.LENGTH_LONG).show()
                    DoctorD.visibility = View.GONE
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(activity, "Failed: ${databaseError.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

}