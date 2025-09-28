package com.example.madd_assignment_1_app_developmment

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.madd_assignment_1_app_developmment.databinding.ActivityUpdateBinding
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class UpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val backButton = findViewById<ImageView>(R.id.backImageButton3)
        backButton.setOnClickListener {
            finish()
        }

        val originalVariety = intent.getStringExtra("paddyVariety")
        val originalDate = intent.getStringExtra("plantingDate")
        val originalFieldSize = intent.getStringExtra("fieldSize")

        val varieties = listOf("BG 352", "BG 358", "BG 360")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, varieties)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.editPaddyVariety.adapter = adapter
        originalVariety?.let {
            val pos = varieties.indexOf(it)
            if (pos >= 0) binding.editPaddyVariety.setSelection(pos)
        }

        binding.editPlantingDate.setText(originalDate)

        binding.editPlantingDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            originalDate?.split("/")?.let { parts ->
                if (parts.size == 3) {
                    calendar.set(parts[2].toInt(), parts[1].toInt() - 1, parts[0].toInt())
                }
            }
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    binding.editPlantingDate.setText(dateFormat.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.editFieldSize.setText(originalFieldSize)

        binding.editeButton.setOnClickListener {
            val updatedVariety = binding.editPaddyVariety.selectedItem.toString()
            val updatedDate = binding.editPlantingDate.text.toString()
            val updatedSize = binding.editFieldSize.text.toString().toDoubleOrNull()

            if (updatedDate.isEmpty() || updatedSize == null) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dbRef = FirebaseDatabase.getInstance()
                .getReference("FarmerPaddyData")
                .child(originalVariety ?: "")

            val updatedData = User(updatedVariety, updatedVariety,updatedDate, updatedSize)

            dbRef.setValue(updatedData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Update Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
