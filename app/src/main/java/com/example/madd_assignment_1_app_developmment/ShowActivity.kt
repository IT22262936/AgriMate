package com.example.madd_assignment_1_app_developmment

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import com.example.madd_assignment_1_app_developmment.databinding.ActivityShowBinding

class ShowActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val backButton = findViewById<ImageView>(R.id.backImageButton)
        backButton.setOnClickListener {
            finish()
        }

        val paddyVarietyStr = intent.getStringExtra("paddyVariety") ?: return
        val fieldSizeStr = intent.getStringExtra("fieldSize") ?: ""
        val plantingDateStr = intent.getStringExtra("plantingDate") ?: return

        binding.paddyVariety.text = "Paddy Variety: $paddyVarietyStr"
        binding.harvestDate.text = "Field Size: $fieldSizeStr"
        binding.wateringDates.text = "Planting Date: $plantingDateStr"

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val plantingDate = dateFormat.parse(plantingDateStr)!!

        database = FirebaseDatabase.getInstance().getReference("Paddy Informations/$paddyVarietyStr")
        database.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val maturityDuration = snapshot.child("maturityDuration").getValue(Int::class.java) ?: 0
                val wateringDays = snapshot.child("wateringDays").children.mapNotNull { it.getValue(Int::class.java) }
                val fertilizer1Day = snapshot.child("fertilizer1Day").getValue(Int::class.java) ?: 0
                val fertilizer1Type = snapshot.child("fertilizer1Type").getValue(String::class.java) ?: ""
                val fertilizer1Amount = snapshot.child("fertilizer1Amount").getValue(Double::class.java) ?: 0.0
                val fertilizer2Day = snapshot.child("fertilizer2Day").getValue(Int::class.java) ?: 0
                val fertilizer2Type = snapshot.child("fertilizer2Type").getValue(String::class.java) ?: ""
                val fertilizer2Amount = snapshot.child("fertilizer2Amount").getValue(Double::class.java) ?: 0.0
                val fertilizer3Day = snapshot.child("fertilizer3Day").getValue(Int::class.java) ?: 0
                val fertilizer3Type = snapshot.child("fertilizer3Type").getValue(String::class.java) ?: ""
                val fertilizer3Amount = snapshot.child("fertilizer3Amount").getValue(Double::class.java) ?: 0.0

                val calendar = Calendar.getInstance()
                calendar.time = plantingDate
                calendar.add(Calendar.DAY_OF_YEAR, maturityDuration)

                val harvestDateStr = dateFormat.format(calendar.time)

                val wateringDatesList = wateringDays.map {
                    calendar.time = plantingDate
                    calendar.add(Calendar.DAY_OF_YEAR, it)
                    dateFormat.format(calendar.time)
                }

                val fertilizerScheduleList = listOf(
                    Triple(fertilizer1Type, fertilizer1Amount, fertilizer1Day),
                    Triple(fertilizer2Type, fertilizer2Amount, fertilizer2Day),
                    Triple(fertilizer3Type, fertilizer3Amount, fertilizer3Day)
                ).map {
                    calendar.time = plantingDate
                    calendar.add(Calendar.DAY_OF_YEAR, it.third)
                    "${dateFormat.format(calendar.time)} - ${it.first} (${it.second} kg/ha)"
                }

                binding.harvestDate.text = "Harvest Date: $harvestDateStr"
                binding.wateringDates.text = "Watering Dates: ${wateringDatesList.joinToString(", ")}"
                binding.fertilizerSchedule.text = "Fertilizer Schedule:\n${fertilizerScheduleList.joinToString("\n")}"
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}
