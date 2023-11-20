package com.kai.padhelper.ui.fragments

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.kai.padhelper.R
import com.kai.padhelper.ui.MainActivity
import com.kai.padhelper.ui.adapters.SearchAdapter
import com.kai.padhelper.ui.viewmodels.SearchViewModel
import com.kai.padhelper.util.RemoteConfig
import com.kai.padhelper.util.Utility.Companion.isValidInt
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchAdapter: SearchAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel = (activity as MainActivity).searchViewModel
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
                val padCharacter = searchAdapter.differ.currentList[position]
                searchViewModel.deleteCharacterSearchResult(padCharacter)
                Snackbar.make(view, "已刪除結果", Snackbar.LENGTH_LONG).apply {
                    setAction("復原") {
                        searchViewModel.saveCharacterSearchResult(padCharacter)
                    }
                    show()
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(padSearchRecyclerView)

        // Observe LiveData
        setPadSearchObserver()

        val urlEditText = view.findViewById<EditText>(R.id.character_id_edit_text)
        urlEditText.setOnEditorActionListener { _, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                keyEvent?.action == KeyEvent.ACTION_DOWN &&
                keyEvent.keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                println("max index: ${RemoteConfig.MAX_CHARACTER_INDEX}")
                if (isValidInt(urlEditText.text.toString())
                    && urlEditText.text?.toString()
                        ?.toLong() in 1..RemoteConfig.MAX_CHARACTER_INDEX!!
                ) {

                    // Create temp null character for instant reaction.
                    val tmpList = searchAdapter.differ.currentList.toMutableList()
                    tmpList.add(0, null)
                    searchAdapter.differ.submitList(tmpList.toList()) {
                        padSearchRecyclerView.scrollToPosition(0)
                    }

                    val queryUrl = "https://pad.chesterip.cc/" + urlEditText.text
                    searchViewModel.fetchData(queryUrl)
                    urlEditText.setText("")
                } else {
                    Toast.makeText(requireContext(), "輸入編號錯誤！", Toast.LENGTH_SHORT).show()
                }
                true
            } else {
                false
            }
        }

        searchAdapter.setOnItemClickListener { padCharacter ->
            val bundle = Bundle().apply {
                putString("characterId", padCharacter.characterId)
            }
            findNavController().navigate(
                R.id.action_searchFragment_to_characterDetailFragment,
                bundle
            )
        }
    }

    private fun setPadSearchObserver() {
        searchViewModel.getSavedSearchResult().observe(viewLifecycleOwner) { newData ->
            searchAdapter.differ.submitList(newData)
        }
    }
}