package com.example.cloudhirepro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class JobsAdapter(options: FirebaseRecyclerOptions<JobsModel>) :
    FirebaseRecyclerAdapter<JobsModel, JobsAdapter.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.job_list_style, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: JobsModel) {
        holder.bind(model)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val jobNameTextView: TextView = itemView.findViewById(R.id.jobName)
        private val jobLocationTextView: TextView = itemView.findViewById(R.id.jobLocation)
        private val jobSalaryTextView: TextView = itemView.findViewById(R.id.jobSalary)
        private val jobCompanyTextView: TextView = itemView.findViewById(R.id.jobCompanyName)
        private val jobDetailsTextView: TextView = itemView.findViewById(R.id.jobDetails)
        private val jobQualificationTextView: TextView = itemView.findViewById(R.id.jobQualification)
        private val applyButton: Button = itemView.findViewById(R.id.jobApplyBtn)
        private val jobCompanyImageView: ImageView = itemView.findViewById(R.id.jobImage)

        fun bind(job: JobsModel) {
            jobNameTextView.text = job.jobname
            jobLocationTextView.text = job.location
            jobSalaryTextView.text = job.salary
            jobCompanyTextView.text = job.company
            jobDetailsTextView.text = job.jobDescriptions
            jobQualificationTextView.text = job.jobQualification

            val storageRef: StorageReference =
                FirebaseStorage.getInstance().getReferenceFromUrl(job.jobCompanyImage)
            Glide.with(itemView.context)
                .load(storageRef)
                .placeholder(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_light)
                .error(R.drawable.ic_launcher_foreground)
                .into(jobCompanyImageView)

            val currentUser = FirebaseAuth.getInstance().currentUser
            val uid = currentUser?.uid
            if (uid != null) {
                val database = FirebaseDatabase.getInstance()
                val appliedJobsRef = database.getReference("users").child(uid).child("appliedJobs").child(job.id)

                appliedJobsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            applyButton.text = "Applied"
                            applyButton.isEnabled = false
                            Toast.makeText(itemView.context, "You have already applied for this job!", Toast.LENGTH_SHORT).show()
                        } else {
                            applyButton.text = "Apply"
                            applyButton.isEnabled = true
                            applyButton.setOnClickListener {
                                applyButton.text = "Applying..."
                                applyButton.isEnabled = false

                                val ref = database.getReference("users").child(uid).child("appliedJobs").child(job.id)
                                ref.setValue(job).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        applyButton.text = "Applied"
                                    } else {
                                        applyButton.text = "Apply"
                                        applyButton.isEnabled = true
                                        Toast.makeText(itemView.context, "Failed to apply. Please try again later.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error
                        applyButton.isEnabled = true
                    }
                })
            }
        }
    }
}