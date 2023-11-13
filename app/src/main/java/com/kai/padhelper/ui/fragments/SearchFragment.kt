package com.kai.padhelper.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kai.padhelper.R
import com.kai.padhelper.ui.MainActivity
import com.kai.padhelper.ui.adapters.SearchAdapter
import com.kai.padhelper.ui.viewmodels.ViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    private lateinit var viewModel: ViewModel
    private lateinit var searchAdapter: SearchAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        // 連接RecyclerView
        val padSearchRecyclerView: RecyclerView = view.findViewById(R.id.padSearchRecyclerView)
        padSearchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        searchAdapter = SearchAdapter()
        padSearchRecyclerView.adapter = searchAdapter
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                viewModel.removeItem(position)
            }
        })
        itemTouchHelper.attachToRecyclerView(padSearchRecyclerView)

        // Observe LiveData
        setPadSearchObserver()

        val urlEditText = view.findViewById<EditText>(R.id.character_id_edit_text)
        val fetchButton: Button = view.findViewById(R.id.fetchButton)
        fetchButton.setOnClickListener {
            if (urlEditText.text.toString() != ""
                && urlEditText.text?.toString()?.toInt() in 1..20000) {
                val tmpList = searchAdapter.differ.currentList.toMutableList()
                tmpList.add(0, null)
                searchAdapter.differ.submitList(tmpList.toList())
                //searchAdapter.notifyDataSetChanged()
                val queryUrl = "https://pad.chesterip.cc/" + urlEditText.text
                viewModel.fetchData(queryUrl)
            } else {
                Toast.makeText(requireContext(), "輸入編號錯誤！", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setPadSearchObserver() {
        viewModel.padSearchListLiveData.observe(viewLifecycleOwner) { newData ->
            searchAdapter.differ.submitList(newData)
            //searchAdapter.setPadSearchList(newData)
            //searchAdapter.notifyDataSetChanged()
        }
    }
}