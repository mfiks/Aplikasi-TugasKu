package com.example.tugasku.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.tugasku.R
import com.example.tugasku.activity.AllActivity
import com.example.tugasku.activity.EditActivity
import com.example.tugasku.adapter.TaskAdapter
import com.example.tugasku.adapter.TaskCompleteAdapter
import com.example.tugasku.database.DatabaseClient
import com.example.tugasku.database.TaskDAO
import com.example.tugasku.database.TaskModel
import com.example.tugasku.databinding.FragmentHomeBinding
import com.example.tugasku.util.dateToLong
import com.example.tugasku.util.dateToday
import kotlin.math.log

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: TaskDAO
    private lateinit var taskSelected: TaskModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        database = DatabaseClient.getService(requireActivity()).taskDAO()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textToday.text = dateToday()
        setupList()
        setupListener()
        setupData()
    }

    override fun onStart() {
        super.onStart()
        setupData()
    }

    private fun setupList(){
        binding.listTask.adapter = taskAdapter
        binding.listTaskCompleted.adapter = taskCompletedAdapter
    }

    private fun setupListener(){
        binding.labelTaskCompleted.setOnClickListener {
            if (binding.listTaskCompleted.visibility == View.GONE){
                binding.listTaskCompleted.visibility = View.VISIBLE
                binding.imageTaskCompleted.setImageResource(R.drawable.ic_arrow_down)
            } else {
                binding.listTaskCompleted.visibility = View.GONE
                binding.imageTaskCompleted.setImageResource(R.drawable.ic_arrow_right)
            }
        }
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addFragment)
            //    testInsert()
        }
        binding.textTask.setOnClickListener {
            startActivity(Intent(requireActivity(), AllActivity::class.java))
        }
    }

    private fun setupData(){
        database.taskAll(
            completed = false,
            date = dateToLong(dateToday())
        ).observe(viewLifecycleOwner, {
            Log.e("taskAll", it.toString())
            taskAdapter.addList(it)
            binding.textAlert.visibility = if  (it.isEmpty()) View.VISIBLE else View.GONE
        })
        database.taskAll(
            completed = true,
            date = dateToLong(dateToday())
        ).observe(viewLifecycleOwner, {
            Log.e("taskAllCompleted", it.toString())
            taskCompletedAdapter.addList(it)
            binding.labelTaskCompleted.visibility = if  (it.isEmpty()) View.GONE else View.VISIBLE
            binding.imageTaskCompleted.visibility = if  (it.isEmpty()) View.GONE else View.VISIBLE
        })
    }

    private val taskAdapter by lazy {
        TaskAdapter(arrayListOf(),object : TaskAdapter.AdapterListener{
            override fun onUpdate(taskModel: TaskModel) {
                taskSelected = taskModel
                taskSelected.completed = true
                Thread {database.update(taskSelected)}.start()
            }
            override fun onDetail(taskModel: TaskModel) {
                startActivity(
                    Intent(requireActivity(), EditActivity::class.java)
                        .putExtra("intent_task", taskModel)
                )
            }
        })
    }

    private val taskCompletedAdapter by lazy {
        TaskCompleteAdapter(arrayListOf(),object : TaskCompleteAdapter.AdapterListener{
            override fun onClick(taskModel: TaskModel) {
                taskSelected = taskModel
                taskSelected.completed = false
                Thread {database.update(taskSelected)}.start()
            }

        })
    }

}