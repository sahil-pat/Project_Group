package com.example.cloudhirepro

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class CandidateDetails : AppCompatActivity() {
    // Initialize views
    private lateinit var connectButton: Button
    private lateinit var candidateDetailName: TextView
    private lateinit var candidateDetailDescription: TextView
    private lateinit var candidateDetailQualification: TextView
    private lateinit var candidateDetailImage: ImageView
    private lateinit var candidateDetailSkillName1: TextView
    private lateinit var candidateDetailSkillLevel1: TextView
    private lateinit var candidateDetailSkillName2: TextView
    private lateinit var candidateDetailSkillLevel2: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.candidate_detail_page)

        // Initialize views
        connectButton = findViewById(R.id.connectButton)
        candidateDetailName = findViewById(R.id.candidateDetailName)
        candidateDetailDescription = findViewById(R.id.candidateDetailDescription)
        candidateDetailQualification = findViewById(R.id.candidateDetailQualification)
        candidateDetailImage = findViewById(R.id.candidateDetailImage)
        candidateDetailSkillName1 = findViewById(R.id.candidateDetailSkillName1)
        candidateDetailSkillLevel1 = findViewById(R.id.candidateDetailSkillLevel1)
        candidateDetailSkillName2 = findViewById(R.id.candidateDetailSkillName2)
        candidateDetailSkillLevel2 = findViewById(R.id.candidateDetailSkillLevel2)

        // Retrieve data from intent
        val name = intent.getStringExtra("name") ?: ""
        val description = intent.getStringExtra("description") ?: ""
        val qualification = intent.getStringExtra("qualification") ?: ""
        val photoUrl = intent.getStringExtra("photoUrl") ?: ""
        val skillID1Name = intent.getStringExtra("skillID1Name") ?: ""
        val skillId1Level = intent.getStringExtra("skillId1Level") ?: ""
        val skillID2Name = intent.getStringExtra("skillID2Name") ?: ""
        val skillId2Level = intent.getStringExtra("skillId2Level") ?: ""

        // Set data to views
        candidateDetailName.text = name
        candidateDetailDescription.text = description
        candidateDetailQualification.text = qualification
        candidateDetailSkillName1.text = skillID1Name
        candidateDetailSkillLevel1.text = skillId1Level
        candidateDetailSkillName2.text = skillID2Name
        candidateDetailSkillLevel2.text = skillId2Level

        connectButton.setOnClickListener {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val userId = currentUser?.uid

            userId?.let { uid ->
                val database = Firebase.database
                val connectionRef = database.reference.child("Connections").child(uid)

                connectionRef.orderByChild("name").equalTo(name).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(baseContext, "Already connected!", Toast.LENGTH_SHORT).show()
                            connectButton.isEnabled = false
                            connectButton.text = "Connected"
                            connectButton.backgroundTintList = ColorStateList.valueOf(Color.GRAY)
                        } else {
                            val connectionId = connectionRef.push().key // Generate a unique ID for the connection
                            val connection = ConnectionModel(connectionId.orEmpty(), name, description, qualification, photoUrl)
                            connectionRef.child(connectionId.orEmpty()).setValue(connection)
                                .addOnSuccessListener {
                                    Toast.makeText(baseContext, "Connection added successfully!", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(baseContext, "Failed to add connection.", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                    }
                })
            }
        }




        val storageRef = Firebase.storage.getReferenceFromUrl(photoUrl)
        Glide.with(this).load(storageRef).into(candidateDetailImage)

        val detailBackBtn = findViewById<ImageView>(R.id.detailBackBtn)
        detailBackBtn.setOnClickListener{
            val intent = Intent(this, CandidateList::class.java)
            startActivity(intent)
        }
    }
}

