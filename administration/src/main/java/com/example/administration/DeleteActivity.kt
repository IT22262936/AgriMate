package com.example.administration

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.administration.databinding.ActivityDeleteBinding
import com.example.administration.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DeleteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeleteBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val backButton = findViewById<ImageView>(R.id.backImageButton2)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.deleteScheduleButton.setOnClickListener {
            val paddyVariety = binding.deletePaddyVariety.text.toString()
            if (paddyVariety.isNotEmpty()){
                deleteData(paddyVariety)
            }else{
                Toast.makeText(this,"Please Enter Paddy Variety", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun deleteData(paddyVariety: String){
        databaseReference = FirebaseDatabase.getInstance().getReference("Paddy Informations")
        databaseReference.child(paddyVariety).removeValue().addOnSuccessListener {
            binding.deletePaddyVariety.text.clear()
            Toast.makeText(this,"Delete", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@DeleteActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }.addOnFailureListener {
            Toast.makeText(this,"Unable to Delete", Toast.LENGTH_SHORT).show()
        }
    }
}