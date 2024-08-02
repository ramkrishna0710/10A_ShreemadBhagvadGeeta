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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import `in`.ram.gita.NetworkManager
import `in`.ram.gita.R
import `in`.ram.gita.databinding.FragmentHomeBinding
import `in`.ram.gita.datasource.api.room.SavedChapters
import `in`.ram.gita.model.Chapters
import `in`.ram.gita.view.adapter.AdapterChapters
import `in`.ram.gita.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding

    private val viewModel : MainViewModel by viewModels()
    private lateinit var adapterChapters : AdapterChapters

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        checkInternetConnectivity()
        changeStatusBarColor()
        showVerseOfTheDay()
        binding.ivSettingHome.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_settingsFragment) }
        return binding.root
    }

    private fun showVerseOfTheDay() {
        val chapterNumber = (1..18).random()
        val verseNumber = (1..18).random()

        lifecycleScope.launch {
            viewModel.getAParticularVerse(chapterNumber,verseNumber).collect{
                for (i in it.translations!!) {
                    if (i?.language == "english") {
                        binding.tvVerseOfTheDay.text = i.description
                        break
                    }
                }
            }
        }
    }

    fun onFavouriteClick(chaptersItem: Chapters.ChaptersItem){
        lifecycleScope.launch {
            viewModel.putSavedChaptersSP(chaptersItem.chapterNumber.toString(),chaptersItem.id!!)
                viewModel.getVerses(chaptersItem.chapterNumber!!).collect{
                    val verseList = arrayListOf<String>()
                    for (currentVerse in it) {
                        for (verses in currentVerse.translations!!){
                            if (verses?.language == "english"){
                                verseList.add(verses.description!!)
                                break
                            }
                        }
                    }

                    val savedChapters = SavedChapters(
                        chapterNumber = chaptersItem.chapterNumber,
                        chapterSummary = chaptersItem.chapterSummary,
                        chapterSummaryHindi = chaptersItem.chapterSummaryHindi,
                        id = chaptersItem.id,
                        name = chaptersItem.name,
                        nameMeaning = chaptersItem.nameMeaning,
                        nameTranslated = chaptersItem.nameTranslated,
                        nameTransliterated = chaptersItem.nameTransliterated,
                        slug = chaptersItem.slug,
                        versesCount = chaptersItem.versesCount,

                        verses = verseList
                    )

                    viewModel.insertChapters(savedChapters)
            }
        }
    }

    private fun onFavouriteFilled(chaptersItem: Chapters.ChaptersItem) {
        lifecycleScope.launch {
            viewModel.deleteSavedChaptersSP(chaptersItem.chapterNumber.toString())
            viewModel.deleteChapter(chaptersItem.id!!)
        }
    }

    private fun checkInternetConnectivity() {
        val networkManager = NetworkManager(requireContext())
        networkManager.observe(viewLifecycleOwner){
            if (it == true){
                binding.shimmer.visibility = View.VISIBLE
                binding.rvChapters.visibility = View.VISIBLE
                binding.tvShowingMessage.visibility = View.GONE
                binding.ivNoInternet.visibility = View.GONE
                getAllChapters()
            }
            else {
                binding.shimmer.visibility = View.GONE
                binding.rvChapters.visibility = View.GONE
                binding.tvShowingMessage.visibility = View.VISIBLE
                binding.ivNoInternet.visibility = View.VISIBLE
            }
        }
    }

    private fun getAllChapters() {
        lifecycleScope.launch {
            viewModel.getAllChapters().collect{chapterList->
                adapterChapters = AdapterChapters(::onChapterIVClicked, ::onFavouriteClick, true,::onFavouriteFilled, viewModel)

                binding.rvChapters.adapter = adapterChapters
                adapterChapters.differ.submitList(chapterList)
                binding.shimmer.visibility = View.GONE
            }
        }
    }

    private fun onChapterIVClicked(chaptersItem: Chapters.ChaptersItem) {
        val bundle = Bundle()
        chaptersItem.chapterNumber?.let { bundle.putInt("chapter_number", it) }
        bundle.putString("chapter_title",chaptersItem.nameTranslated)
        bundle.putString("chapter_description",chaptersItem.chapterSummary)
        bundle.putInt("verse_count", chaptersItem.versesCount!!)

        findNavController().navigate(R.id.action_homeFragment_to_versesFragment, bundle)
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