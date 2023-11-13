package com.kai.padhelper.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kai.padhelper.R
import com.kai.padhelper.data.model.PadSearchModel
import javax.inject.Inject

class SearchAdapter @Inject constructor() :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<PadSearchModel>() {
        override fun areItemsTheSame(oldItem: PadSearchModel, newItem: PadSearchModel): Boolean {
            return oldItem.characterId == newItem.characterId
        }

        override fun areContentsTheSame(oldItem: PadSearchModel, newItem: PadSearchModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pad_search, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        println("onBindViewHolder, position: $position")
        val character = differ.currentList[position]

        if (character == null) {
            holder.nameTextView.text = "Loading..."
            holder.iconImageView.setImageResource(R.drawable.placeholder_image)
            holder.typeImagesLinearLayout.removeAllViews()
            holder.awokenImagesLinearLayout.removeAllViews()
            holder.superAwokenImagesLinearLayout.removeAllViews()
            holder.skillCdTextView.text = ""
            holder.characterId.text = ""
        } else {
            holder.nameTextView.text = character.name
            Glide.with(holder.iconImageView.context)
                .load(character.iconUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(holder.iconImageView)
            holder.typeImagesLinearLayout.removeAllViews()
            holder.awokenImagesLinearLayout.removeAllViews()
            holder.superAwokenImagesLinearLayout.removeAllViews()
            character.typeUrls?.let { urls ->
                for (url in urls) {
                    val imageView = ImageView(holder.typeImagesLinearLayout.context)
                    imageView.layoutParams = LinearLayout.LayoutParams(24.toDp(imageView), 24.toDp(imageView))
                    imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                    Glide.with(holder.typeImagesLinearLayout.context)
                        .load(url)
                        .placeholder(R.drawable.placeholder_image)
                        .into(imageView)
                    holder.typeImagesLinearLayout.addView(imageView)
                }
            }
            character.awokenUrls?.let { urls ->
                for (url in urls) {
                    val imageView = ImageView(holder.awokenImagesLinearLayout.context)
                    imageView.layoutParams = LinearLayout.LayoutParams(24.toDp(imageView), 24.toDp(imageView))
                    imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                    Glide.with(holder.awokenImagesLinearLayout.context)
                        .load(url)
                        .placeholder(R.drawable.placeholder_image)
                        .into(imageView)
                    holder.awokenImagesLinearLayout.addView(imageView)
                }
            }
            character.superAwokenUrls?.let { urls ->
                for (url in urls) {
                    val imageView = ImageView(holder.superAwokenImagesLinearLayout.context)
                    imageView.layoutParams = LinearLayout.LayoutParams(24.toDp(imageView), 24.toDp(imageView))
                    imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                    Glide.with(holder.superAwokenImagesLinearLayout.context)
                        .load(url)
                        .placeholder(R.drawable.placeholder_image)
                        .into(imageView)
                    holder.superAwokenImagesLinearLayout.addView(imageView)
                }
            }
            if (character.skillCd?.first != null) {
                holder.skillCdTextView.text = "Skill: ${character.skillCd?.first}->${character.skillCd?.second}"
            } else {
                holder.skillCdTextView.text = "Skill: ${character.skillCd?.second}"
            }
            holder.characterId.text = "No. " + character.characterId
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val iconImageView: ImageView = itemView.findViewById(R.id.iconImageView)
        val typeImagesLinearLayout: LinearLayout = itemView.findViewById(R.id.typeImagesLinearLayout)
        val awokenImagesLinearLayout: LinearLayout = itemView.findViewById(R.id.awokenImagesLinearLayout)
        val superAwokenImagesLinearLayout: LinearLayout = itemView.findViewById(R.id.superAwokenImagesLinearLayout)
        val skillCdTextView: TextView = itemView.findViewById(R.id.skillCdTextView)
        val characterId: TextView = itemView.findViewById(R.id.character_id)
    }

    private fun Int.toDp(view: View): Int {
        val density = view.context.resources.displayMetrics.density
        return (this * density).toInt()
    }
}