package com.example.cloudhirepro

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase

class JobsList : AppCompatActivity() {

    private lateinit var adapter: JobsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.job_list_page)

        val query = FirebaseDatabase.getInstance().reference.child("jobs")
        val options = FirebaseRecyclerOptions.Builder<JobsModel>()
            .setQuery(query, JobsModel::class.java)
            .build()

        adapter = JobsAdapter(options)

        val recyclerView: RecyclerView = findViewById(R.id.rViewJobs)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val backButtonJobs = findViewById<FloatingActionButton>(R.id.backButtonJobs)
        backButtonJobs.setOnClickListener{
            val intent= Intent(this, HomePage::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}
