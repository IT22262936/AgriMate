package com.example.administration

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.administration.databinding.ActivityUpdateBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance().getReference("Paddy Informations")

        binding.updateSheduleButton.setOnClickListener {
            val paddyVariety = binding.updatePaddyVariety.text.toString().trim()
            if (paddyVariety.isEmpty()) {
                Toast.makeText(this, "Paddy Variety is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val maturityDuration = binding.updateMaturityDuration.text.toString().toIntOrNull()
            val wateringDays = binding.updateWateringDays.text.toString()
                .split(",")
                .mapNotNull { it.trim().toIntOrNull() }

            val fertilizer1Day = binding.updateFertilizer1Day.text.toString().toIntOrNull()
            val fertilizer1Type = binding.updateFertilizer1Type.text.toString().takeIf { it.isNotEmpty() }
            val fertilizer1Amount = binding.updateFertilizer1Amount.text.toString().toDoubleOrNull()

            val fertilizer2Day = binding.updateFertilizer2Day.text.toString().toIntOrNull()
            val fertilizer2Type = binding.updateFertilizer2Type.text.toString().takeIf { it.isNotEmpty() }
            val fertilizer2Amount = binding.updateFertilizer2Amount.text.toString().toDoubleOrNull()

            val fertilizer3Day = binding.updateFertilizer3Day.text.toString().toIntOrNull()
            val fertilizer3Type = binding.updateFertilizer3Type.text.toString().takeIf { it.isNotEmpty() }
            val fertilizer3Amount = binding.updateFertilizer3Amount.text.toString().toDoubleOrNull()

            updateData(
                paddyVariety,
                maturityDuration,
                wateringDays,
                fertilizer1Day,
                fertilizer1Type,
                fertilizer1Amount,
                fertilizer2Day,
                fertilizer2Type,
                fertilizer2Amount,
                fertilizer3Day,
                fertilizer3Type,
                fertilizer3Amount
            )
        }
    }

    private fun updateData(
        paddyVariety: String,
        maturityDuration: Int?,
        wateringDays: List<Int>?,
        fertilizer1Day: Int?,
        fertilizer1Type: String?,
        fertilizer1Amount: Double?,
        fertilizer2Day: Int?,
        fertilizer2Type: String?,
        fertilizer2Amount: Double?,
        fertilizer3Day: Int?,
        fertilizer3Type: String?,
        fertilizer3Amount: Double?
    ) {
        val updateMap = mutableMapOf<String, Any>()

        maturityDuration?.let { updateMap["maturityDuration"] = it }
        if (!wateringDays.isNullOrEmpty()) updateMap["wateringDays"] = wateringDays

        fertilizer1Day?.let { updateMap["fertilizer1Day"] = it }
        fertilizer1Type?.let { updateMap["fertilizer1Type"] = it }
        fertilizer1Amount?.let { updateMap["fertilizer1Amount"] = it }

        fertilizer2Day?.let { updateMap["fertilizer2Day"] = it }
        fertilizer2Type?.let { updateMap["fertilizer2Type"] = it }
        fertilizer2Amount?.let { updateMap["fertilizer2Amount"] = it }

        fertilizer3Day?.let { updateMap["fertilizer3Day"] = it }
        fertilizer3Type?.let { updateMap["fertilizer3Type"] = it }
        fertilizer3Amount?.let { updateMap["fertilizer3Amount"] = it }

        if (updateMap.isEmpty()) {
            Toast.makeText(this, "No Fields to Update", Toast.LENGTH_SHORT).show()
            return
        }

        databaseReference.child(paddyVariety).updateChildren(updateMap)
            .addOnSuccessListener {
                clearFields()
                Toast.makeText(this, "Update Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@UpdateActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearFields() {
        binding.updatePaddyVariety.text.clear()
        binding.updateMaturityDuration.text.clear()
        binding.updateWateringDays.text.clear()
        binding.updateFertilizer1Day.text.clear()
        binding.updateFertilizer1Type.text.clear()
        binding.updateFertilizer1Amount.text.clear()
        binding.updateFertilizer2Day.text.clear()
        binding.updateFertilizer2Type.text.clear()
        binding.updateFertilizer2Amount.text.clear()
        binding.updateFertilizer3Day.text.clear()
        binding.updateFertilizer3Type.text.clear()
        binding.updateFertilizer3Amount.text.clear()
    }
}
