package com.example.tugasku.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.tugasku.R
import com.example.tugasku.database.DatabaseClient
import com.example.tugasku.database.TaskDAO
import com.example.tugasku.database.TaskModel
import com.example.tugasku.databinding.FragmentAddBinding
import com.example.tugasku.util.dateToDialog
import com.example.tugasku.util.dateToLong
import com.example.tugasku.util.dateToString
import com.example.tugasku.util.dateToday


class AddFragment : Fragment() {

    private lateinit var binding: FragmentAddBinding
    private lateinit var database: TaskDAO

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater, container, false)
        database = DatabaseClient.getService(requireActivity()).taskDAO()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textDate.text = dateToday()
        binding.labelDate.setOnClickListener{
            val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                binding.textDate.text = dateToString(year, month, dayOfMonth)
            }
            dateToDialog(requireActivity(), datePicker).show()
        }
        binding.buttonSave.setOnClickListener{
            val task = TaskModel (
                0,
                binding.editTask.text.toString(),
                false,
                dateToLong(binding.textDate.text.toString())
            )

            Thread {
                database.insert(task)
                requireActivity().runOnUiThread {
                    Toast.makeText(requireActivity(), "Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }.start()
        }
    }
}