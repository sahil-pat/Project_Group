package com.example.cloudhirepro

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase

class CandidateList : AppCompatActivity() {

    private var adapter: CandidateAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.candidate_list_page)
        FirebaseApp.initializeApp(this)

        val query = FirebaseDatabase.getInstance().reference.child("candidates")
        val options = FirebaseRecyclerOptions.Builder<Candidates>()
            .setQuery(query, Candidates::class.java)
            .build()
        adapter = CandidateAdapter(options)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewAll)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val candidateBackBtn = findViewById<FloatingActionButton>(R.id.backButton)
        candidateBackBtn.setOnClickListener{
            val intent=Intent(this, HomePage::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }
}