package com.example.madd_assignment_1_app_developmment

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class MyAdapter(
    private val userList : ArrayList<User>
    ) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.acivity_user_item,
            parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {

        val currentitem = userList[position]

        holder.paddyVariety.text = currentitem.paddyVariety
        holder.plantingDate.text = currentitem.plantingDate
        holder.fieldSize.text = currentitem.fieldSize.toString()

        // ✅ Add click listener to open ShowActivity
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ShowActivity::class.java)
            intent.putExtra("paddyVariety", currentitem.paddyVariety)
            intent.putExtra("fieldSize", currentitem.fieldSize.toString())
            intent.putExtra("plantingDate", currentitem.plantingDate)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val paddyVariety : TextView = itemView.findViewById(R.id.paddyVariety)
        val plantingDate: TextView = itemView.findViewById(R.id.plantingDate)
        val fieldSize: TextView = itemView.findViewById(R.id.fieldSize)
    }
}