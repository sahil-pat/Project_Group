package com.example.cloudhirepro

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Profile : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView
    private lateinit var mainAddressEditText: EditText
    private lateinit var subAddressEditText: EditText
    private lateinit var number:EditText
    private lateinit var description:EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_page)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        profileName = findViewById(R.id.profileName)
        profileEmail = findViewById(R.id.profileEmail)
        mainAddressEditText = findViewById(R.id.profileNewMainAddress)
        subAddressEditText= findViewById(R.id.profileAddressSub)
        number = findViewById(R.id.profileNumber)
        description= findViewById(R.id.profileDetailDescription)
        saveButton = findViewById(R.id.profileSaveBtn)


        saveButton.setOnClickListener {
            updateMainAddress()
        }

        val currentUser = auth.currentUser
        currentUser?.let {
            val userId = it.uid
            val userRef = database.child("Users").child(userId)

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(Users::class.java)
                        user?.let {
                            val fullName = "${user.firstName} ${user.lastName}"
                            profileName.text = fullName
                            profileEmail.text = user.email
                            mainAddressEditText.setText(user.mainAddress)
                            subAddressEditText.setText(user.subAddress)
                            number.setText(user.number)
                            description.setText(user.description)
                        }
                    } else {
                        Log.d(TAG, "No data found for current user")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value", error.toException())
                }
            })
        }
        val profileBackBtn = findViewById<ImageView>(R.id.profileBackBtn)
        profileBackBtn.setOnClickListener{
            val intent= Intent(this,HomePage::class.java)
            startActivity(intent)
        }
    }

    private fun updateMainAddress() {
        val currentUser = auth.currentUser
        currentUser?.let {
            val userId = it.uid
            val userRef = database.child("Users").child(userId)

            val newMainAddress = mainAddressEditText.text.toString().trim()
            val newSubAddress = subAddressEditText.text.toString().trim()
            val newNumber = number.text.toString().trim()
            val newDescription = description.text.toString().trim()

            val updates = hashMapOf<String, Any>(
                "mainAddress" to newMainAddress,
                "subAddress" to newSubAddress,
                "number" to newNumber,
                "description" to newDescription
            )

            userRef.updateChildren(updates)
                .addOnSuccessListener {
                    Log.d(TAG, "Profile updated")
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Log.e(TAG, "Failed to update main address", it)
                    Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
        }
    }


    companion object {
        private const val TAG = "Profile"
    }
}

