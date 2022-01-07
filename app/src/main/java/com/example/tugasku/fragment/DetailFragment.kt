package com.example.tugasku.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.tugasku.R
import com.example.tugasku.database.TaskModel
import com.example.tugasku.databinding.FragmentDetailBinding
import com.example.tugasku.util.dateToString

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var detail: TaskModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        detail = requireActivity().intent.getSerializableExtra("intent_task") as TaskModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textTask.text = detail.task
        binding.textDate.text = dateToString(detail.date)
        binding.imageEdit.setOnClickListener{
            findNavController()
                .navigate(
                    R.id.action_detailFragment_to_updateFragment,
                    bundleOf("argument_task" to detail)
                )
        }
    }
}