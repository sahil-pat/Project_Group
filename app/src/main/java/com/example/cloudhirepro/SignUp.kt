package com.example.cloudhirepro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {

    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var confirmPasswordEditText: TextInputEditText
    private lateinit var firstNameEditText: TextInputEditText
    private lateinit var lastNameEditText: TextInputEditText
    private lateinit var signUpButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_page)

        emailEditText = findViewById(R.id.emailEt)
        passwordEditText = findViewById(R.id.passET)
        confirmPasswordEditText = findViewById(R.id.confirmPassEt)
        firstNameEditText = findViewById(R.id.firstNameEt)
        lastNameEditText = findViewById(R.id.lastNameEt)
        signUpButton = findViewById(R.id.signInButton)
        auth = FirebaseAuth.getInstance()

        signUpButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()
            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()

            if (firstName.isEmpty() || lastName.isEmpty()) {
                Toast.makeText(baseContext, "Please enter your first and last name.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(baseContext, "Password must be at least 6 characters long.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(baseContext, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = auth.currentUser
                        val userId = firebaseUser?.uid
                        val user = Users(firstName, lastName, email, password)

                        if (userId != null) {
                            val database = FirebaseDatabase.getInstance().reference
                            database.child("Users").child(userId).setValue(user)
                                .addOnSuccessListener {
                                    Toast.makeText(baseContext, "User information saved successfully!", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(baseContext, "Failed to save user information.", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        Toast.makeText(baseContext, "Sign up failed. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        val signInUser = findViewById<TextView>(R.id.signInUser)
        signInUser.setOnClickListener {
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}