package com.example.madd_assignment_1_app_developmment

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class UserListActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_list)

        val backButton = findViewById<ImageView>(R.id.backImageButton2)
        backButton.setOnClickListener { finish() }

        userRecyclerView = findViewById(R.id.userList)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.setHasFixedSize(true)
        userArrayList = arrayListOf()

        getUserData()
    }

    private fun getUserData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("FarmerPaddyData")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the list before adding updated data
                userArrayList.clear()

                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(User::class.java)
                        user?.let { userArrayList.add(it) }
                    }
                }

                userRecyclerView.adapter = MyAdapter(userArrayList)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
