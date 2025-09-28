package com.example.madd_assignment_1_app_developmment

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class MyAdapter(
    private val userList: ArrayList<User>
) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.acivity_user_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]

        holder.paddyVariety.text = currentItem.paddyVariety
        holder.plantingDate.text = currentItem.plantingDate
        holder.fieldSize.text = currentItem.fieldSize.toString()

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ShowActivity::class.java)
            intent.putExtra("paddyVariety", currentItem.paddyVariety)
            intent.putExtra("fieldSize", currentItem.fieldSize.toString())
            intent.putExtra("plantingDate", currentItem.plantingDate)
            context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener {
            val dbRef = FirebaseDatabase.getInstance()
                .getReference("FarmerPaddyData")
                .child(currentItem.paddyVariety ?: "")

            dbRef.removeValue()
                .addOnSuccessListener {
                    userList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, userList.size)
                    Toast.makeText(holder.itemView.context, "Deleted successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(holder.itemView.context, "Delete failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }

        holder.editButton.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, UpdateActivity::class.java)
            intent.putExtra("paddyVariety", currentItem.paddyVariety)
            intent.putExtra("fieldSize", currentItem.fieldSize.toString())
            intent.putExtra("plantingDate", currentItem.plantingDate)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val paddyVariety: TextView = itemView.findViewById(R.id.paddyVariety)
        val plantingDate: TextView = itemView.findViewById(R.id.plantingDate)
        val fieldSize: TextView = itemView.findViewById(R.id.fieldSize)
        val editButton: ImageButton = itemView.findViewById(R.id.editButton)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
    }
}
