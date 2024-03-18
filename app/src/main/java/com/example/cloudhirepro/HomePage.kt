package com.example.cloudhirepro

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class HomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homepage_layout)

        val jobButton = findViewById<ImageButton>(R.id.button_jobs)
        jobButton.setOnClickListener{
            val intent= Intent(this, JobsList::class.java)
            startActivity(intent)
        }

        val candidateButton = findViewById<ImageButton>(R.id.button_candidates)
        candidateButton.setOnClickListener {
            val intent = Intent(this, CandidateList::class.java)
            startActivity(intent)
        }

        val profileButton = findViewById<ImageButton>(R.id.button_profile)
        profileButton.setOnClickListener {
            val intent= Intent(this, Profile::class.java)
            startActivity(intent)
        }
        val buttonConnections = findViewById<ImageButton>(R.id.button_connections)
        buttonConnections.setOnClickListener {
            val intent= Intent(this, ConnectionList::class.java)
            startActivity(intent)
        }

        val logoutButton = findViewById<ImageButton>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this, "Log out successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Finish the current activity to prevent going back to it when pressing back
        }

    }
}
