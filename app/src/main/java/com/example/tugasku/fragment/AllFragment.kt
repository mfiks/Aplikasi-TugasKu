package com.example.tugasku.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.navigation.fragment.findNavController
import com.example.tugasku.R
import com.example.tugasku.activity.AllActivity
import com.example.tugasku.activity.EditActivity
import com.example.tugasku.adapter.TaskAdapter
import com.example.tugasku.adapter.TaskCompleteAdapter
import com.example.tugasku.database.DatabaseClient
import com.example.tugasku.database.TaskDAO
import com.example.tugasku.database.TaskModel
import com.example.tugasku.databinding.ActivityAllBinding
import com.example.tugasku.databinding.FragmentAllBinding
import java.util.zip.Inflater

class AllFragment : Fragment() {

    private lateinit var binding: FragmentAllBinding
    private lateinit var database: TaskDAO
    private lateinit var taskSelected: TaskModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllBinding.inflate(inflater, container, false)
        database = DatabaseClient.getService(requireActivity()).taskDAO()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        binding.imageMenu.setOnClickListener{ imageMenu ->
            PopupMenu(requireActivity(), imageMenu) .apply {
                setOnMenuItemClickListener { item ->
                    when(item?.itemId) {
                        R.id.action_new -> {
                            findNavController().navigate(R.id.action_allFragment_to_addFragment)
                            true
                        }
                        R.id.action_delete -> {
                            Thread {database.deleteCompleted()}.start()
                            true
                        }
                        R.id.action_delete_all -> {
                            Thread {database.deleteAll()}.start()
                            true
                        }
                        else -> false
                    }
                }
                inflate(R.menu.menu_task_all)
                show()
            }
        }
        binding.labelTaskCompleted.setOnClickListener {
            if (binding.listTaskCompleted.visibility == View.GONE){
                binding.listTaskCompleted.visibility = View.VISIBLE
                binding.imageTaskCompleted.setImageResource(R.drawable.ic_arrow_down)
            } else {
                binding.listTaskCompleted.visibility = View.GONE
                binding.imageTaskCompleted.setImageResource(R.drawable.ic_arrow_right)
            }
        }
    }

    private fun setupData(){
        database.taskAll(false).observe(viewLifecycleOwner, {
            Log.e("taskAll", it.toString())
            taskAdapter.addList(it)
            binding.textAlert.visibility = if  (it.isEmpty()) View.VISIBLE else View.GONE
        })
        database.taskAll(true).observe(viewLifecycleOwner, {
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