package com.kai.padhelper.ui.fragments

import android.os.Bundle
import android.text.InputType
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.kai.padhelper.R
import com.kai.padhelper.data.model.TeamRecord
import com.kai.padhelper.databinding.FragmentTeamRecordBinding
import com.kai.padhelper.ui.MainActivity
import com.kai.padhelper.ui.adapters.TeamRecordAdapter
import com.kai.padhelper.ui.viewmodels.RecordViewModel
import com.kai.padhelper.util.Utility.Companion.extractCharacterIdFromIconUrl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeamRecordFragment : Fragment(R.layout.fragment_team_record) {
    private lateinit var viewModel: RecordViewModel
    private lateinit var recordAdapter: TeamRecordAdapter
    private var _viewBinding: FragmentTeamRecordBinding? = null
    private val binding get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentTeamRecordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).recordViewModel
        recordAdapter = TeamRecordAdapter { teamRecord, clickedView, url ->
            onCharacterIconClickListener(teamRecord, clickedView, url)
        }
        binding.teamRecordRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = recordAdapter
        }
        getItemTouchHelper(view).attachToRecyclerView(binding.teamRecordRecyclerView)
        setRecordObserver()
        setFabOnClickListener()
    }

    private fun getItemTouchHelper(view: View): ItemTouchHelper {
        return ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
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
                val teamRecord = recordAdapter.differ.currentList[position]
                viewModel.deleteTeamRecord(teamRecord)
                showDeleteTeamConfirmDialog(view, teamRecord)
            }
        })
    }
    private fun setRecordObserver() {
        viewModel.getSavedRecords().observe(viewLifecycleOwner) { newData ->
            recordAdapter.differ.submitList(newData)
            binding.emptyView.visibility = if (newData.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setFabOnClickListener() {
        binding.fab.setOnClickListener {
            viewModel.saveTeamRecord(TeamRecord())
        }
    }

    private fun onCharacterIconClickListener(teamRecord: TeamRecord, view: View, url: String?) {
        val viewName = resources.getResourceEntryName(view.id)
        if (viewName == "imgBadge") {
            viewModel.saveTeamRecord(teamRecord)
        } else if (viewName == "textTeamName" || viewName == "txtTeamInfo") {
            showEditDialog(teamRecord, viewName)
        } else if (url == null || url == "null") {
            showSearchIdDialog(teamRecord, viewName, view)
        } else {
            val popupMenu = PopupMenu(requireContext(), view)
            popupMenu.menuInflater.inflate(R.menu.team_record_popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.set_character -> {
                        showSearchIdDialog(teamRecord, viewName,view)
                    }
                    R.id.character_detail -> {
                        val bundle = Bundle().apply {
                            putString("characterId", extractCharacterIdFromIconUrl(url))
                        }
                        findNavController().navigate(
                            R.id.action_teamRecordFragment_to_characterDetailFragment,
                            bundle
                        )
                    }
                }
                true
            }
            popupMenu.show()
        }
    }

    private fun showSearchIdDialog(teamRecord: TeamRecord, viewName: String, view: View) {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_input_id, null)
        val editTextRoleId = dialogLayout.findViewById<EditText>(R.id.editTextRoleId)

        with(builder) {
            setView(dialogLayout)
            setPositiveButton("確定") { _, _ ->
                (view as ImageView).setImageResource(R.drawable.loading)
                val id = editTextRoleId.text.toString()
                viewModel.queryCharacterId(teamRecord, viewName, id)
            }
            setNegativeButton("取消", null)
            show()
        }
    }

    private fun showEditDialog(teamRecord: TeamRecord, viewName: String) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_text,null)
        val editText = dialogView.findViewById<EditText>(R.id.editTextDialogUserInput)
        val title: String
        when (viewName) {
            "textTeamName" -> {
                title = "輸入隊伍名稱"
                editText.setText(teamRecord.teamName)
            }
            "txtTeamInfo" -> {
                title = "輸入隊伍紀錄"
                editText.setText(teamRecord.recordText)
                editText.minLines = 10
                editText.maxLines = 20
                editText.gravity = Gravity.TOP
                editText.inputType =  InputType.TYPE_CLASS_TEXT or
                        InputType.TYPE_TEXT_FLAG_MULTI_LINE
                editText.movementMethod = ScrollingMovementMethod()
            }
            else -> title = ""
        }
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setView(dialogView)
            .setPositiveButton("確定") { dialog, _ ->
                val updateRecord = when (viewName) {
                    "textTeamName" -> teamRecord.copy(teamName = editText.text.toString())
                    "txtTeamInfo" -> teamRecord.copy(recordText = editText.text.toString())
                    else -> teamRecord
                }
                viewModel.saveTeamRecord(updateRecord)
                dialog.dismiss()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun showDeleteTeamConfirmDialog(curView: View, teamRecord: TeamRecord) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_confirm_delete,null)
        AlertDialog.Builder(requireContext())
            .setTitle("刪除隊伍警告")
            .setView(dialogView)
            .setPositiveButton("確定") { dialog, _ ->
                Snackbar.make(curView, "已刪除紀錄", Snackbar.LENGTH_LONG).apply {
                    setAction("復原") {
                        viewModel.saveTeamRecord(teamRecord)
                    }
                    show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("取消") { dialog, _ ->
                viewModel.saveTeamRecord(teamRecord)
                dialog.dismiss()
            }
            .show()
    }
}