package com.example.cloudhirepro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.storage

class ConnectionAdapter : RecyclerView.Adapter<ConnectionAdapter.ConnectionViewHolder>() {

    private var connectionList: List<ConnectionModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.connection_list_style, parent, false)
        return ConnectionViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConnectionViewHolder, position: Int) {
        val connection = connectionList[position]
        holder.bind(connection)
    }

    override fun getItemCount(): Int {
        return connectionList.size
    }

    fun setData(connectionList: List<ConnectionModel>) {
        this.connectionList = connectionList
        notifyDataSetChanged()
    }

    class ConnectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val connectionNameTextView: TextView = itemView.findViewById(R.id.ConnectionName)
        private val connectionDetailTextView: TextView = itemView.findViewById(R.id.connectionDetail)
        private val connectionQualificationTextView: TextView = itemView.findViewById(R.id.connectionQualification)
        private val connectionImageView: ImageView = itemView.findViewById(R.id.connectionImage)
        private val connectionMsgImg: ImageButton = itemView.findViewById(R.id.connectionMsgImg)
        private val connectionMessage: TextInputEditText = itemView.findViewById(R.id.connectionMessage)
        private val previousMessagesTextView: TextView = itemView.findViewById(R.id.previousMessagesTextView)

        fun bind(connection: ConnectionModel) {
            connectionNameTextView.text = connection.name
            connectionDetailTextView.text = connection.detail
            connectionQualificationTextView.text = connection.qualification

            val storageRef = Firebase.storage.getReferenceFromUrl(connection.photoUrl)
            Glide.with(itemView.context)
                .load(storageRef)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(connectionImageView)

            connectionMsgImg.setOnClickListener {
                val message = connectionMessage.text.toString()
                saveMessageToDatabase(connection.id, message)
                connectionMessage.setText("")
            }

            loadPreviousMessages(connection.id)
        }

        private fun loadPreviousMessages(connectionId: String) {
            val database = FirebaseDatabase.getInstance()
            val currentUser = Firebase.auth.currentUser
            val uid = currentUser?.uid

            if (uid != null) {
                val connectionsRef = database.getReference("Connections").child(uid).child(connectionId).child("messages")
                connectionsRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val messages = mutableListOf<String>()
                        for (data in snapshot.children) {
                            val message = data.getValue(String::class.java)
                            message?.let {
                                messages.add(it)
                            }
                        }
                        val previousMessages = messages.joinToString("\n")
                        previousMessagesTextView.text = previousMessages
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
        }

        private fun saveMessageToDatabase(connectionId: String, message: String) {
            val database = FirebaseDatabase.getInstance()
            val currentUser = Firebase.auth.currentUser
            val uid = currentUser?.uid

            if (uid != null) {
                val connectionsRef = database.getReference("Connections").child(uid).child(connectionId).child("messages")
                connectionsRef.push().setValue(message)
            }
        }
    }
}
