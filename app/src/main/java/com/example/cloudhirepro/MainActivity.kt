package com.example.cloudhirepro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var logInButton: Button
    private lateinit var signUpButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        emailEditText = findViewById(R.id.emailEt)
        passwordEditText = findViewById(R.id.passET)
        logInButton = findViewById(R.id.logInButton)
        signUpButton = findViewById(R.id.signUpButton)
        auth = FirebaseAuth.getInstance()

        logInButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(baseContext, "Please fill in the email and password.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        val user = auth.currentUser
                        Toast.makeText(baseContext, "Authentication success.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, HomePage::class.java)
                        startActivity(intent)
                    } else {

                        Toast.makeText(baseContext, "Authentication failed. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }
        }


        signUpButton.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
    }
}

