package com.kai.padhelper.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kai.padhelper.R
import com.kai.padhelper.data.model.TeamRecord
import com.kai.padhelper.databinding.ItemTeamRecordBinding

class TeamRecordAdapter(
    private val onViewClicked: (teamRecord: TeamRecord, view: View, url: String?) -> Unit
) : RecyclerView.Adapter<TeamRecordAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<TeamRecord>() {
        override fun areItemsTheSame(oldItem: TeamRecord, newItem: TeamRecord): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TeamRecord, newItem: TeamRecord): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTeamRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val teamRecord = differ.currentList[position]
        holder.bind(teamRecord)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ViewHolder(private val binding: ItemTeamRecordBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(teamRecord: TeamRecord) {
            // 徽章
            loadBadgeImage(binding.imgBadge.context, teamRecord.badgeIconUrl, binding.imgBadge)
            binding.imgBadge.setOnClickListener {
                showBadgeDialog(teamRecord)
            }
            // 隊長
            loadImage(binding.imgLeader.context, teamRecord.leaderUrl, binding.imgLeader)
            binding.imgLeader.setOnClickListener {
                onViewClicked(teamRecord, binding.imgLeader, teamRecord.leaderUrl)
            }
            // 隊員1
            loadImage(binding.imgMember1.context, teamRecord.member1Url, binding.imgMember1)
            binding.imgMember1.setOnClickListener {
                onViewClicked(teamRecord, binding.imgMember1, teamRecord.member1Url)
            }
            // 隊員2
            loadImage(binding.imgMember2.context, teamRecord.member2Url, binding.imgMember2)
            binding.imgMember2.setOnClickListener {
                onViewClicked(teamRecord, binding.imgMember2, teamRecord.member2Url)
            }
            // 隊員3
            loadImage(binding.imgMember3.context, teamRecord.member3Url, binding.imgMember3)
            binding.imgMember3.setOnClickListener {
                onViewClicked(teamRecord, binding.imgMember3, teamRecord.member3Url)
            }
            // 隊員4
            loadImage(binding.imgMember4.context, teamRecord.member4Url, binding.imgMember4)
            binding.imgMember4.setOnClickListener {
                onViewClicked(teamRecord, binding.imgMember4, teamRecord.member4Url)
            }
            // 好友隊長
            loadImage(binding.imgViceCaptain.context, teamRecord.viceLeaderIconUrl, binding.imgViceCaptain)
            binding.imgViceCaptain.setOnClickListener {
                onViewClicked(teamRecord, binding.imgViceCaptain, teamRecord.viceLeaderIconUrl)
            }
            // 隊伍名稱
            if (teamRecord.teamName == null || teamRecord.teamName == "null") {
                binding.textTeamName.text = binding.textTeamName.context.getText(R.string.team_name)
            } else {
                binding.textTeamName.text = teamRecord.teamName
            }
            binding.textTeamName.setOnClickListener {
                onViewClicked(teamRecord, binding.textTeamName, teamRecord.teamName)
            }
            // 隊伍紀錄
            binding.txtTeamInfo.setOnClickListener {
                onViewClicked(teamRecord, binding.txtTeamInfo, teamRecord.recordText)
            }
        }

        @SuppressLint("CheckResult")
        private fun loadImage(context: Context, url: String?, imageView: ImageView) {
            val placeholder = if (url.isNullOrEmpty()) android.R.drawable.ic_menu_add else R.drawable.loading
            Glide.with(context)
                .load(url)
                .placeholder(placeholder)
                .into(imageView)
        }

        private fun showBadgeDialog(teamRecord: TeamRecord) {
            val dialogView = LayoutInflater.from(itemView.context).inflate(R.layout.dialog_badge,null)
            val dialog = AlertDialog.Builder(itemView.context)
                .setTitle("選擇隊伍徽章")
                .setView(dialogView)
                .create()
            dialog.setOnShowListener {
                val imageViews = getAllImageViews(dialog.window!!.decorView)
                imageViews.forEach { imageView ->
                    imageView.setOnClickListener {
                        handleImageClick(teamRecord, imageView.id)
                        dialog.dismiss()
                    }
                }
            }
            dialog.show()
        }

        private fun getAllImageViews(view: View): List<ImageView> {
            val imageViews = mutableListOf<ImageView>()
            if (view is ViewGroup) {
                for (i in 0 until view.childCount) {
                    val child = view.getChildAt(i)
                    if (child is ImageView) {
                        imageViews.add(child)
                    } else if (child is ViewGroup) {
                        imageViews.addAll(getAllImageViews(child))
                    }
                }
            }
            return imageViews
        }

        private fun handleImageClick(teamRecord: TeamRecord, imageViewId: Int) {
            val badgeType: String = when (imageViewId) {
                R.id.pass -> "pass"
                R.id.move -> "move"
                R.id.aoe -> "aoe"
                R.id.heal25 -> "heal25"
                R.id.hp5 -> "hp5"
                R.id.atk5 -> "atk5"
                R.id.sb -> "sb"
                R.id.bound -> "bound"
                R.id.sx -> "sx"
                R.id.drop -> "drop"
                R.id.heal35 -> "heal35"
                R.id.hp15 -> "hp15"
                R.id.atk15 -> "atk15"
                R.id.move_plus -> "move_plus"
                R.id.exp -> "exp"
                R.id.dark -> "dark"
                R.id.disturb -> "disturb"
                R.id.poison -> "poison"
                else -> "null"
            }
            val updateRecord = teamRecord.copy(badgeIconUrl = badgeType)
            onViewClicked(updateRecord, binding.imgBadge, badgeType)
        }

        private fun loadBadgeImage(context: Context, url: String?, imageView: ImageView) {
            val drawable = when (url) {
                "pass" -> R.drawable.pass
                "move" -> R.drawable.move
                "aoe" -> R.drawable.aoe
                "heal25" -> R.drawable.heal25
                "hp5" -> R.drawable.hp5
                "atk5" -> R.drawable.atk5
                "sb" -> R.drawable.sb
                "bound" -> R.drawable.bound
                "sx" -> R.drawable.sx
                "drop" -> R.drawable.drop
                "heal35" -> R.drawable.heal35
                "hp15" -> R.drawable.hp15
                "atk15" -> R.drawable.atk15
                "move_plus" -> R.drawable.move_plus
                "exp" -> R.drawable.exp
                "dark" -> R.drawable.dark
                "disturb" -> R.drawable.disturb
                "poison" -> R.drawable.poison
                else -> R.drawable.pass
            }
            Glide.with(context)
                .load(drawable)
                .placeholder(R.drawable.loading)
                .into(imageView)
        }
    }
}