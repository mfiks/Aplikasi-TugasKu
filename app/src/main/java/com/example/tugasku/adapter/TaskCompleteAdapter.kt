package com.example.tugasku.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasku.database.TaskModel
import com.example.tugasku.databinding.AdapterTaskCompleteBinding
import com.example.tugasku.util.dateToString

class TaskCompleteAdapter(
    var items:ArrayList<TaskModel>,
    var listener: AdapterListener,
) : RecyclerView.Adapter<TaskCompleteAdapter.ViewHolder>() {

    class ViewHolder(val binding: AdapterTaskCompleteBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        AdapterTaskCompleteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.textTask.text = item.task
        holder.binding.textTask.paintFlags =
            holder.binding.textTask.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.binding.textDate.text = dateToString(item.date)
        holder.binding.imageTask.setOnClickListener {
            listener.onClick(item)
        }
    }

    override fun getItemCount() = items.size

    fun addList(list: List<TaskModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    interface AdapterListener{
        fun onClick( taskModel: TaskModel)
    }

}