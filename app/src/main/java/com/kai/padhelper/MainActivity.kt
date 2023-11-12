package com.kai.padhelper

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kai.padhelper.ui.adapters.PadSearchAdapter
import com.kai.padhelper.ui.viewmodels.PadSearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var padSearchAdapter: PadSearchAdapter

    private val padSearchViewModel: PadSearchViewModel by viewModels()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 連接RecyclerView
        val padSearchRecyclerView: RecyclerView = findViewById(R.id.padSearchRecyclerView)
        padSearchRecyclerView.layoutManager = LinearLayoutManager(this)
        padSearchRecyclerView.adapter = padSearchAdapter
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
                padSearchViewModel.removeItem(position)
            }
        })
        itemTouchHelper.attachToRecyclerView(padSearchRecyclerView)

        // Observe LiveData
        setPadSearchObserver()

        val urlEditText = findViewById<EditText>(R.id.character_id_edit_text)
        val fetchButton: Button = findViewById(R.id.fetchButton)
        fetchButton.setOnClickListener {
            if (urlEditText.text.toString() != ""
                && urlEditText.text?.toString()?.toInt() in 1..10497) {
                val tmpList = padSearchAdapter.getPadSearchList()
                tmpList.add(0, null)
                padSearchAdapter.setPadSearchList(tmpList)
                padSearchAdapter.notifyDataSetChanged()
                val queryUrl = "https://pad.chesterip.cc/" + urlEditText.text + "/"
                padSearchViewModel.fetchData(queryUrl)
            } else {
                Toast.makeText(this, "輸入編號錯誤！", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setPadSearchObserver() {
        padSearchViewModel.padSearchListLiveData.observe(this, Observer { newData ->
            padSearchAdapter.setPadSearchList(newData)
            padSearchAdapter.notifyDataSetChanged()
        })
    }
}