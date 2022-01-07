package com.example.tugasku.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tugasku.R
import com.example.tugasku.database.DatabaseClient
import com.example.tugasku.database.TaskDAO
import com.example.tugasku.database.TaskModel
import com.example.tugasku.databinding.FragmentUpdateBinding
import com.example.tugasku.util.dateToDialog
import com.example.tugasku.util.dateToLong
import com.example.tugasku.util.dateToString

class UpdateFragment : Fragment() {

    private lateinit var binding: FragmentUpdateBinding
    private lateinit var database: TaskDAO
    private lateinit var detail: TaskModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateBinding.inflate(inflater, container, false)
        database = DatabaseClient.getService(requireActivity()).taskDAO()
        detail = requireArguments().getSerializable("argument_task") as TaskModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpData()
        setUpListener()
    }

    private fun setUpData(){
        binding.editTask.setText(detail.task)
        binding.textDate.text = dateToString(detail.date)
    }

    private fun setUpListener(){
        binding.labelDate.setOnClickListener {
            val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                binding.textDate.text = dateToString(year, month, dayOfMonth)
            }
            dateToDialog(requireActivity(), datePicker).show()
        }
        binding.buttonSave.setOnClickListener {
            detail.task = binding.editTask.text.toString()
            detail.date = dateToLong(binding.textDate.text.toString())
            Thread{
                database.update(detail)
                requireActivity().runOnUiThread {
                    Toast.makeText(requireActivity(), "Perubahan disimpan",Toast.LENGTH_SHORT).show()
                    requireActivity().finish()
                }
            }.start()
        }
        binding.buttonDelete.setOnClickListener {
            Thread{
                database.delete(detail)
                requireActivity().runOnUiThread {
                    Toast.makeText(requireActivity(), "Berhasil dihapus",Toast.LENGTH_SHORT).show()
                    requireActivity().finish()
                }
            }.start()
        }
    }

}