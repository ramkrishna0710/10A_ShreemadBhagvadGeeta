package `in`.ram.gita.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import `in`.ram.gita.databinding.ItemViewChaptersBinding
import `in`.ram.gita.model.Chapters
import `in`.ram.gita.viewmodel.MainViewModel

class AdapterChapters(
    val onChapterIVClicked: (Chapters.ChaptersItem) -> Unit,
    val onFavouriteClick: (Chapters.ChaptersItem) -> Unit,
    val showSavedButton: Boolean,
    val onFavouriteFilled: (Chapters.ChaptersItem) -> Unit,
    val viewModel: MainViewModel,
) : RecyclerView.Adapter<AdapterChapters.ChaptersViewHolder>(){
    class ChaptersViewHolder(val binding : ItemViewChaptersBinding) : ViewHolder(binding.root)

    val diffUtil = object : DiffUtil.ItemCallback<Chapters.ChaptersItem>(){
        override fun areItemsTheSame(
            oldItem: Chapters.ChaptersItem,
            newItem: Chapters.ChaptersItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Chapters.ChaptersItem,
            newItem: Chapters.ChaptersItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChaptersViewHolder {
        return ChaptersViewHolder(ItemViewChaptersBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ChaptersViewHolder, position: Int) {
        val chapter = differ.currentList[position]

        val keys = viewModel.getAllSavedChaptersKeySP()

        if (!showSavedButton) {
            holder.binding.apply {
                ivFavouriteSC.visibility = View.GONE
                ivFavouriteFilledSC.visibility = View.GONE
            }
        } else {
            if (keys.contains(chapter.chapterNumber.toString())) {
                holder.binding.apply {
                    ivFavouriteSC.visibility = View.GONE
                    ivFavouriteFilledSC.visibility = View.VISIBLE
                }
            } else {
                holder.binding.apply {
                    ivFavouriteSC.visibility = View.VISIBLE
                    ivFavouriteFilledSC.visibility = View.GONE
                }
            }
        }

        holder.binding.apply {
            tvChapterNumber.text = "Chapter ${chapter.chapterNumber}"
            tvChapterTitle.text = chapter.nameTranslated
            tvChapterDescription.text = chapter.chapterSummary
            tvVerseCount.text = chapter.versesCount.toString()
        }

        holder.binding.ll.setOnClickListener{
            onChapterIVClicked(chapter)
        }

        holder.binding.apply {
            ivFavouriteSC.setOnClickListener {
                ivFavouriteFilledSC.visibility = View.VISIBLE
                ivFavouriteSC.visibility = View.GONE
                onFavouriteClick(chapter)
            }

            ivFavouriteFilledSC.setOnClickListener {
                ivFavouriteSC.visibility = View.VISIBLE
                ivFavouriteFilledSC.visibility = View.GONE
                onFavouriteFilled(chapter)
            }
        }
    }
}