package com.example.cloudhirepro

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class CandidateAdapter(options: FirebaseRecyclerOptions<Candidates>) :
    FirebaseRecyclerAdapter<Candidates, CandidateAdapter.MyViewHolder>(options) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Candidates) {
        val candidateId = snapshots.getSnapshot(position).key
        val storageRef: StorageReference =
            FirebaseStorage.getInstance().getReferenceFromUrl(model.photoUrl)
        Glide.with(holder.itemView.context).load(storageRef).into(holder.candidateImage)
        holder.candidateName.text = model.name
        holder.candidateDetails.text = model.description
        holder.candidateQualification.text = model.qualification
        holder.candidateSkillName1.text = model.skillID1Name
        holder.candidateSkillLevel1.text = model.skillId1Level
        holder.candidateSkillName2.text = model.skillID2Name
        holder.candidateSkillLevel2.text = model.skillId2Level


        // Set click listener
        holder.itemView.setOnClickListener {

            val intent = Intent(holder.itemView.context, CandidateDetails::class.java)
            intent.putExtra("id", candidateId)
            intent.putExtra("name", model.name)
            intent.putExtra("description", model.description)
            intent.putExtra("qualification", model.qualification)
            intent.putExtra("photoUrl", model.photoUrl)
            intent.putExtra("skillID1Name",model.skillID1Name)
            intent.putExtra("skillId1Level",model.skillId1Level)
            intent.putExtra("skillID2Name",model.skillID2Name)
            intent.putExtra("skillId2Level",model.skillId2Level)
            holder.itemView.context.startActivity(intent)
        }

    }

    class MyViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.candidates_list_style, parent, false)) {
        val candidateName: TextView = itemView.findViewById(R.id.candidateName)
        val candidateDetails: TextView = itemView.findViewById(R.id.candidateDetails)
        val candidateQualification: TextView = itemView.findViewById(R.id.candidateQualification)
        val candidateImage: ImageView = itemView.findViewById(R.id.candidateImage)
        val candidateSkillName1: TextView = itemView.findViewById(R.id.candidateSkillName1)
        val candidateSkillLevel1: TextView = itemView.findViewById(R.id.candidateSkillLevel1)
        val candidateSkillName2: TextView = itemView.findViewById(R.id.candidateSkillName2)
        val candidateSkillLevel2: TextView = itemView.findViewById(R.id.candidateSkillLevel2)

    }
}
