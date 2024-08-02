package `in`.ram.gita.view.fregments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import `in`.ram.gita.R
import `in`.ram.gita.databinding.FragmentSaveChaptersBinding
import `in`.ram.gita.model.Chapters
import `in`.ram.gita.view.adapter.AdapterChapters
import `in`.ram.gita.viewmodel.MainViewModel

class SaveChaptersFragment : Fragment() {
    private lateinit var binding: FragmentSaveChaptersBinding
    private lateinit var adapterChapters: AdapterChapters
    private val viewModel : MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSaveChaptersBinding.inflate(layoutInflater)

        changeStatusBarColor()
        getSavedChapters()
        return binding.root
    }

    private fun getSavedChapters() {
        viewModel.getChapters().observe(viewLifecycleOwner){
            val chapterList = arrayListOf<Chapters.ChaptersItem>()
            for (i in it) {
                val chaptersItem = Chapters.ChaptersItem(
                    i.chapterNumber,
                    i.chapterSummary,
                    i.chapterSummaryHindi,
                    i.id,
                    i.name,
                    i.nameMeaning,
                    i.nameTranslated,
                    i.nameTransliterated,
                    i.slug,
                    i.versesCount
                )
                chapterList.add(chaptersItem)
            }

            if (chapterList.isEmpty()) {
                binding.shimmer.visibility = View.GONE
                binding.rvChapters.visibility = View.GONE
                binding.tvShowingMessage.visibility = View.VISIBLE
            }

            adapterChapters = AdapterChapters(
                ::onChapterItemViewClicked,
                ::onFavClicked,
                false,
                ::onFavouriteFilled,
                viewModel
            )
            binding.rvChapters.adapter = adapterChapters
            adapterChapters.differ.submitList(chapterList)

            binding.shimmer.visibility = View.GONE
            binding.rvChapters.visibility = View.VISIBLE
        }
    }

    fun onChapterItemViewClicked(chaptersItem: Chapters.ChaptersItem){
        val bundle = Bundle()
        chaptersItem.chapterNumber?.let { bundle.putInt("chapter_number", it) }

        bundle.putBoolean("showRoomData",true)

        findNavController().navigate(R.id.action_saveChaptersFragment_to_versesFragment,bundle)
    }

    fun onFavClicked(chaptersItem: Chapters.ChaptersItem){

    }
    fun onFavouriteFilled(chaptersItem: Chapters.ChaptersItem){

    }

    private fun changeStatusBarColor() {
        val window = activity?.window
        window ?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window ?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
        if (window != null) {
            WindowCompat.getInsetsController(window,window.decorView).apply {
                isAppearanceLightStatusBars = true
            }
        }
    }
}