package com.example.administration

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.administration.databinding.ActivityMainBinding
import com.example.administration.databinding.ActivityUploadBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val backButton = findViewById<ImageView>(R.id.backImageButton3)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.saveScheduleButton.setOnClickListener {
            val paddyVariety = binding.paddyVariety.text.toString()
            val maturityDuration = binding.maturityDuration.text.toString().toIntOrNull()
            val wateringDays = binding.wateringDays.text.toString()
                .split(",")
                .mapNotNull { it.trim().toIntOrNull() }
            val fertilizer1Day = binding.fertilizer1Day.text.toString().toIntOrNull()
            val fertilizer1Type = binding.fertilizer1Type.text.toString()
            val fertilizer1Amount = binding.fertilizer1Amount.text.toString().toDoubleOrNull()
            val fertilizer2Day = binding.fertilizer2Day.text.toString().toIntOrNull()
            val fertilizer2Type = binding.fertilizer2Type.text.toString()
            val fertilizer2Amount = binding.fertilizer2Amount.text.toString().toDoubleOrNull()
            val fertilizer3Day = binding.fertilizer3Day.text.toString().toIntOrNull()
            val fertilizer3Type = binding.fertilizer3Type.text.toString()
            val fertilizer3Amount = binding.fertilizer3Amount.text.toString().toDoubleOrNull()

            databaseReference = FirebaseDatabase.getInstance().getReference("Paddy Informations")
            val paddyData = PaddyData(
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
                fertilizer3Amount)

            databaseReference.child(paddyVariety).setValue(paddyData)
                .addOnSuccessListener {
                binding.paddyVariety.text.clear()
                binding.maturityDuration.text.clear()
                binding.wateringDays.text.clear()
                binding.fertilizer1Day.text.clear()
                binding.fertilizer1Type.text.clear()
                binding.fertilizer1Amount.text.clear()
                binding.fertilizer2Day.text.clear()
                binding.fertilizer2Type.text.clear()
                binding.fertilizer2Amount.text.clear()
                binding.fertilizer3Day.text.clear()
                binding.fertilizer3Type.text.clear()
                binding.fertilizer3Amount.text.clear()
                Toast.makeText(this,"Save", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@UploadActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
                .addOnFailureListener {
                Toast.makeText(this,"Failed", Toast.LENGTH_SHORT).show()
            }
        }

    }
}