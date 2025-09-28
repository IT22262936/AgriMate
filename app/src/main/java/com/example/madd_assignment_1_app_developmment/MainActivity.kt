package com.example.madd_assignment_1_app_developmment

import android.content.Intent
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.madd_assignment_1_app_developmment.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import android.widget.ArrayAdapter
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference
    private var paddyVarieties = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("Paddy Informations")

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                paddyVarieties.clear()
                for (child in snapshot.children) {
                    child.key?.let { paddyVarieties.add(it) }
                }
                val adapter = ArrayAdapter(
                    this@MainActivity,
                    android.R.layout.simple_spinner_item,
                    paddyVarieties
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.selectPaddyVariety.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to load varieties", Toast.LENGTH_SHORT).show()
            }
        })

        binding.plantingDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${month + 1}/$year"
                    binding.plantingDate.setText(selectedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.submitButton.setOnClickListener {
            val selectedVariety = binding.selectPaddyVariety.selectedItem.toString()
            val plantingDate = binding.plantingDate.text.toString()
            val fieldSize = binding.fieldSize.text.toString().toDoubleOrNull()

            if (selectedVariety.isEmpty() || plantingDate.isEmpty() || fieldSize == null) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val farmerData = FarmerPaddyData(selectedVariety, plantingDate, fieldSize)
            FirebaseDatabase.getInstance().getReference("FarmerPaddyData")
                .push()
                .setValue(farmerData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show()
                    // Move to Schedule Page
                    val intent = Intent(this, ShowActivity::class.java)
                    intent.putExtra("paddyVariety", selectedVariety)
                    intent.putExtra("plantingDate", plantingDate)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }

        binding.viewAllPaddyDataButton.setOnClickListener {
            val intent = Intent(this, UserListActivity::class.java)
            startActivity(intent)
        }
    }
}
