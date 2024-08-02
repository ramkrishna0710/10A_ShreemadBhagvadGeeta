package `in`.ram.gita.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import `in`.ram.gita.databinding.ItemViewVersesBinding
import `in`.ram.gita.datasource.api.room.SavedVerses

class AdapterSavedVerses(val onVerseItemVClicked: (SavedVerses) -> Unit) :RecyclerView.Adapter<AdapterSavedVerses.SavedVerseseViewHolder>() {
    class SavedVerseseViewHolder(val binding: ItemViewVersesBinding) : ViewHolder(binding.root)

    val diffUtil = object : DiffUtil.ItemCallback<SavedVerses>(){
        override fun areItemsTheSame(oldItem: SavedVerses, newItem: SavedVerses): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SavedVerses, newItem: SavedVerses): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedVerseseViewHolder {
        return SavedVerseseViewHolder(ItemViewVersesBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: SavedVerseseViewHolder, position: Int) {
        val verse = differ.currentList[position]
        holder.binding.tvVerseNumber.text = "Verse ${verse.chapterNumber}.${verse.verseNumber}"
        holder.binding.tvVerse.text = verse.translations!![0]?.description

        holder.binding.ll.setOnClickListener {
                onVerseItemVClicked(verse)
        }
    }

}