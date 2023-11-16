package com.kai.padhelper.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
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
import com.kai.padhelper.data.model.PadCharacter
import javax.inject.Inject

class SearchAdapter @Inject constructor() :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<PadCharacter>() {
        override fun areItemsTheSame(oldItem: PadCharacter, newItem: PadCharacter): Boolean {
            return oldItem.characterId == newItem.characterId
        }

        override fun areContentsTheSame(oldItem: PadCharacter, newItem: PadCharacter): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pad_search, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = differ.currentList[position]
        holder.bind(character)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val iconImageView: ImageView = itemView.findViewById(R.id.iconImageView)
        private val typeImagesLinearLayout: LinearLayout = itemView.findViewById(R.id.typeImagesLinearLayout)
        private val awokenImagesLinearLayout: LinearLayout = itemView.findViewById(R.id.awokenImagesLinearLayout)
        private val superAwokenImagesLinearLayout: LinearLayout = itemView.findViewById(R.id.superAwokenImagesLinearLayout)
        private val skillCdTextView: TextView = itemView.findViewById(R.id.skillCdTextView)
        private val characterId: TextView = itemView.findViewById(R.id.character_id)

        @SuppressLint("SetTextI18n")
        fun bind(padCharacter: PadCharacter?) {
            if (padCharacter == null) {
                // 设置为默认加载状态
                setupLoadingState()
            } else {
                nameTextView.text = padCharacter.name
                loadImage(iconImageView.context, padCharacter.iconUrl, iconImageView, true)
                updateLinearLayout(typeImagesLinearLayout, padCharacter.typeUrls)
                updateLinearLayout(awokenImagesLinearLayout, padCharacter.awokenUrls)
                updateLinearLayout(superAwokenImagesLinearLayout, padCharacter.superAwokenUrls)
                skillCdTextView.text = getSkillCdText(padCharacter)
                characterId.text = "No." + padCharacter.characterId
                itemView.setOnClickListener {
                    onItemClickListener?.let { it(padCharacter) }
                }
            }
        }

        private fun setupLoadingState() {
            nameTextView.text = "讀取中..."
            iconImageView.setImageResource(R.drawable.placeholder_image)
            typeImagesLinearLayout.removeAllViews()
            awokenImagesLinearLayout.removeAllViews()
            superAwokenImagesLinearLayout.removeAllViews()
            skillCdTextView.text = ""
            characterId.text = ""
        }

        @SuppressLint("CheckResult")
        private fun loadImage(context: Context, url: String?, imageView: ImageView,
                              withPlaceholder: Boolean = false) {
            val glideRequest = Glide.with(context).load(url)
            if (withPlaceholder) {
                glideRequest.placeholder(R.drawable.placeholder_image)
            }
            glideRequest.into(imageView)
        }

        private fun updateLinearLayout(linearLayout: LinearLayout, urls: List<String>?) {
            linearLayout.removeAllViews()
            urls?.let {
                for (url in it) {
                    val imageView = ImageView(linearLayout.context)
                    imageView.layoutParams = LinearLayout.LayoutParams(24.toDp(imageView), 24.toDp(imageView))
                    imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                    loadImage(linearLayout.context, url, imageView)
                    linearLayout.addView(imageView)
                }
            }
        }

        private fun getSkillCdText(character: PadCharacter): String {
            return character.skillCd?.let {
                if (it.first != null && it.first != "null") "Skill: ${it.first}->${it.second}" else "Skill: ${it.second}"
            } ?: ""
        }

        private fun Int.toDp(view: View): Int {
            val density = view.context.resources.displayMetrics.density
            return (this * density).toInt()
        }
    }

    private var onItemClickListener: ((PadCharacter) -> Unit)? = null
    fun setOnItemClickListener(listener: ((PadCharacter) -> Unit)) {
        onItemClickListener = listener
    }
}