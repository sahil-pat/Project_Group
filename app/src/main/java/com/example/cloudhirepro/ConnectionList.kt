package com.example.cloudhirepro

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ConnectionList : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ConnectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.connection_list)

        recyclerView = findViewById(R.id.connectionRecycleView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ConnectionAdapter()
        recyclerView.adapter = adapter


        val currentUser = Firebase.auth.currentUser
        val uid = currentUser?.uid

        if (uid != null) {
            val database = FirebaseDatabase.getInstance()
            val connectionsRef = database.getReference("Connections").child(uid)

            connectionsRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val connectionList = mutableListOf<ConnectionModel>()

                    for (data in snapshot.children) {
                        val id = data.key
                        val name = data.child("name").getValue(String::class.java)
                        val detail = data.child("detail").getValue(String::class.java) ?: ""
                        val qualification = data.child("qualification").getValue(String::class.java) ?: ""
                        val photoUrl = data.child("photoUrl").getValue(String::class.java) ?: ""

                        if (id != null && name != null) {
                            val connection = ConnectionModel(id, name, detail, qualification, photoUrl)
                            connectionList.add(connection)
                        }
                    }

                    adapter.setData(connectionList)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                }
            })
            val connectionPageBackButton = findViewById<FloatingActionButton>(R.id.connectionPageBackButton)
            connectionPageBackButton.setOnClickListener{
                val intent= Intent(this, HomePage::class.java)
                startActivity(intent)
            }
        }
    }
}
