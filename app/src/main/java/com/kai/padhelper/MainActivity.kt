package com.kai.padhelper

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kai.padhelper.dagger.DaggerPadSearchComponent
import com.kai.padhelper.ui.adapters.PadSearchAdapter
import com.kai.padhelper.ui.viewmodels.PadSearchViewModel
import com.kai.padhelper.ui.viewmodels.PadSearchViewModelFactory
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var viewModelFactory: PadSearchViewModelFactory
    @Inject
    lateinit var padSearchAdapter: PadSearchAdapter
    private lateinit var mPadSearchViewModel: PadSearchViewModel

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // init viewModel
        DaggerPadSearchComponent.create().inject(this)
        mPadSearchViewModel = ViewModelProvider(this, viewModelFactory)[PadSearchViewModel::class.java]

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
                mPadSearchViewModel.removeItem(position)
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
                mPadSearchViewModel.fetchData(queryUrl)
            } else {
                Toast.makeText(this, "輸入編號錯誤！", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setPadSearchObserver() {
        mPadSearchViewModel.padSearchListLiveData.observe(this, Observer { newData ->
            padSearchAdapter.setPadSearchList(newData)
            padSearchAdapter.notifyDataSetChanged()
        })
    }
}