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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import `in`.ram.gita.NetworkManager
import `in`.ram.gita.R
import `in`.ram.gita.databinding.FragmentVerseDetailBinding
import `in`.ram.gita.datasource.api.room.SavedVerses
import `in`.ram.gita.model.Verses
import `in`.ram.gita.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class VerseDetailFragment : Fragment() {

    private lateinit var binding: FragmentVerseDetailBinding
    private val viewModel : MainViewModel by viewModels()

    private var chapternumber = 0
    private var versenumber = 0
    private var verseDetail = MutableLiveData<Verses.VersesItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVerseDetailBinding.inflate(layoutInflater)
        changeStatusBarColor()
        getAndSetChapterAndVerseNumber()
        onReadMoreClick()
        getData()

        onSaveVerse()
        return binding.root
    }

    private fun getData() {
        val bundle = arguments
        val showRoomData = bundle?.getBoolean("showRoomData", false)
        if (showRoomData == true) {
            binding.ivFavouriteVerse.visibility = View.GONE
            binding.ivFavouriteVerseFilled.visibility = View.GONE
            viewModel.getParticularVerse(chapternumber,versenumber).observe(viewLifecycleOwner){verse->

                binding.tvVerseText.text = verse.text
                binding.tvTranslationIfEnglish.text = verse.transliteration
                binding.tvWordIfEnglish.text = verse.wordMeanings

                val englishTranslationList = arrayListOf<Verses.VersesItem.Translation>()

                for (i in verse.translations!!){
                    if (i?.language == "english") {
                        englishTranslationList?.add(i)
                    }
                }
                val englishTranslationListSize = englishTranslationList.size

                if (englishTranslationList.isNotEmpty()) {
                    binding.tvAuthorName.text = englishTranslationList[0].authorName
                    binding.tvText.text = englishTranslationList[0].description

                    if (englishTranslationListSize == 1) {
                        binding.fabTranslationRight.visibility = View.GONE
                    }

                    var i = 0
                    binding.fabTranslationRight.setOnClickListener {
                        if (i < englishTranslationListSize-1) {
                            i++
                            binding.tvAuthorName.text = englishTranslationList[i].authorName
                            binding.tvText.text = englishTranslationList[i].description
                            binding.fabTranslationLeft.visibility = View.VISIBLE

                            if (i== englishTranslationListSize-1){
                                binding.fabTranslationRight.visibility = View.GONE
                            }
                        }
                    }
                    binding.fabTranslationLeft.setOnClickListener {
                        if (i > 0){
                            i--
                            binding.tvAuthorName.text = englishTranslationList[i].authorName
                            binding.tvText.text = englishTranslationList[i].description
                            binding.fabTranslationRight.visibility = View.VISIBLE
                            if (i==0){
                                binding.fabTranslationLeft.visibility = View.GONE
                            }
                        }
                    }
                }

                val englishCommentaryList = arrayListOf<Verses.VersesItem.Commentary>()

                for (i in verse.commentaries!!){
                    if (i?.language == "hindi") {
                        englishCommentaryList?.add(i)
                    }
                }

                val englishCommentaryListSize = englishCommentaryList.size
                if (englishCommentaryList.isNotEmpty()){
                    binding.tvAuthorCommentary.text = englishCommentaryList[0].authorName
                    binding.tvTextCommentary.text = englishCommentaryList[0].description
                    if (englishCommentaryListSize == 1) binding.fabCommentaryRight.visibility = View.GONE

                    var i = 0
                    binding.fabCommentaryRight.setOnClickListener {
                        if (i < englishCommentaryListSize-1) {
                            i++
                            binding.tvAuthorNameCommentary.text = englishCommentaryList[i].authorName
                            binding.tvTextCommentary.text = englishCommentaryList[i].description
                            binding.fabCommentaryLeft.visibility = View.VISIBLE

                            if (i== englishCommentaryListSize-1){
                                binding.fabCommentaryRight.visibility = View.GONE
                            }
                        }
                    }
                    binding.fabCommentaryLeft.setOnClickListener {
                        if (i > 0){
                            i--
                            binding.tvAuthorNameCommentary.text = englishCommentaryList[i].authorName
                            binding.tvTextCommentary.text = englishCommentaryList[i].description
                            binding.fabCommentaryRight.visibility = View.VISIBLE
                            if (i==0){
                                binding.fabCommentaryLeft.visibility = View.GONE
                            }
                        }
                    }
                }

                binding.progressBar.visibility=View.GONE

                binding.tvVerseNumber.visibility = View.VISIBLE
                binding.tvVerseText.visibility = View.VISIBLE
                binding.tvTranslationIfEnglish.visibility = View.VISIBLE
                binding.tvWordIfEnglish.visibility = View.VISIBLE
                binding.view.visibility = View.VISIBLE
                binding.tvTranslation.visibility = View.VISIBLE
                binding.clTranslation.visibility = View.VISIBLE
                binding.tvCommentries.visibility = View.VISIBLE
                binding.clCommentries.visibility = View.VISIBLE
                binding.backgroundImage.visibility = View.VISIBLE
                binding.ivFavouriteVerse.visibility = View.VISIBLE

            }
        } else {
            checkInternetConnectivity()
        }
    }

    private fun onSaveVerse() {

        binding.ivFavouriteVerseFilled.setOnClickListener {
            binding.ivFavouriteVerse.visibility = View.VISIBLE
            binding.ivFavouriteVerseFilled.visibility = View.GONE
            deleteVerse()
        }

        binding.ivFavouriteVerse.setOnClickListener {

            binding.ivFavouriteVerse.visibility = View.GONE
            binding.ivFavouriteVerseFilled.visibility = View.VISIBLE
            savingVerseDetails()
        }
    }

    private fun deleteVerse() {
        lifecycleScope.launch {
            viewModel.deleteAParticularVerse(chapternumber,versenumber)
        }
    }

    private fun savingVerseDetails() {
        verseDetail.observe(viewLifecycleOwner){
            val englishTranslationList = arrayListOf<Verses.VersesItem.Translation>()
            for (i in it.translations!!){
                if (i?.language == "english") {
                    englishTranslationList?.add(i)
                }
            }

            val englishCommentaryList = arrayListOf<Verses.VersesItem.Commentary>()
            for (i in it.commentaries!!){
                if (i?.language == "hindi") {
                    englishCommentaryList?.add(i)
                }
            }

            val savedVerses = SavedVerses(it.chapterNumber,
                englishCommentaryList,
                it.id,
                it.slug,
                it.text,
                englishTranslationList,
                it.transliteration,
                it.verseNumber,
                it.wordMeanings
            )

            lifecycleScope.launch { viewModel.insertEnglishVerse(savedVerses) }
        }
    }

    private fun getVerseDetail() {
        lifecycleScope.launch {
            viewModel.getAParticularVerse(chapternumber,versenumber).collect{verse ->
                verseDetail.postValue(verse)
                binding.tvVerseText.text = verse.text
                binding.tvTranslationIfEnglish.text = verse.transliteration
                binding.tvWordIfEnglish.text = verse.wordMeanings

                val englishTranslationList = arrayListOf<Verses.VersesItem.Translation>()

                for (i in verse.translations!!){
                    if (i?.language == "english") {
                        englishTranslationList?.add(i)
                    }
                }
                val englishTranslationListSize = englishTranslationList.size

                if (englishTranslationList.isNotEmpty()) {
                    binding.tvAuthorName.text = englishTranslationList[0].authorName
                    binding.tvText.text = englishTranslationList[0].description

                    if (englishTranslationListSize == 1) {
                        binding.fabTranslationRight.visibility = View.GONE
                    }

                    var i = 0
                    binding.fabTranslationRight.setOnClickListener {
                        if (i < englishTranslationListSize-1) {
                            i++
                            binding.tvAuthorName.text = englishTranslationList[i].authorName
                            binding.tvText.text = englishTranslationList[i].description
                            binding.fabTranslationLeft.visibility = View.VISIBLE

                            if (i== englishTranslationListSize-1){
                                binding.fabTranslationRight.visibility = View.GONE
                            }
                        }
                    }
                    binding.fabTranslationLeft.setOnClickListener {
                        if (i > 0){
                            i--
                            binding.tvAuthorName.text = englishTranslationList[i].authorName
                            binding.tvText.text = englishTranslationList[i].description
                            binding.fabTranslationRight.visibility = View.VISIBLE
                            if (i==0){
                                binding.fabTranslationLeft.visibility = View.GONE
                            }
                        }
                    }
                }

                val englishCommentaryList = arrayListOf<Verses.VersesItem.Commentary>()

                for (i in verse.commentaries!!){
                    if (i?.language == "hindi") {
                        englishCommentaryList?.add(i)
                    }
                }

                val englishCommentaryListSize = englishCommentaryList.size
                if (englishCommentaryList.isNotEmpty()){
                    binding.tvAuthorCommentary.text = englishCommentaryList[0].authorName
                    binding.tvTextCommentary.text = englishCommentaryList[0].description
                    if (englishCommentaryListSize == 1) binding.fabCommentaryRight.visibility = View.GONE

                    var i = 0
                    binding.fabCommentaryRight.setOnClickListener {
                        if (i < englishCommentaryListSize-1) {
                            i++
                            binding.tvAuthorNameCommentary.text = englishCommentaryList[i].authorName
                            binding.tvTextCommentary.text = englishCommentaryList[i].description
                            binding.fabCommentaryLeft.visibility = View.VISIBLE

                            if (i== englishCommentaryListSize-1){
                                binding.fabCommentaryRight.visibility = View.GONE
                            }
                        }
                    }
                    binding.fabCommentaryLeft.setOnClickListener {
                        if (i > 0){
                            i--
                            binding.tvAuthorNameCommentary.text = englishCommentaryList[i].authorName
                            binding.tvTextCommentary.text = englishCommentaryList[i].description
                            binding.fabCommentaryRight.visibility = View.VISIBLE
                            if (i==0){
                                binding.fabCommentaryLeft.visibility = View.GONE
                            }
                        }
                    }
                }

                binding.progressBar.visibility=View.GONE

                binding.tvVerseNumber.visibility = View.VISIBLE
                binding.tvVerseText.visibility = View.VISIBLE
                binding.tvTranslationIfEnglish.visibility = View.VISIBLE
                binding.tvWordIfEnglish.visibility = View.VISIBLE
                binding.view.visibility = View.VISIBLE
                binding.tvTranslation.visibility = View.VISIBLE
                binding.clTranslation.visibility = View.VISIBLE
                binding.tvCommentries.visibility = View.VISIBLE
                binding.clCommentries.visibility = View.VISIBLE
                binding.backgroundImage.visibility = View.VISIBLE
                binding.ivFavouriteVerse.visibility = View.VISIBLE
            }
        }
    }

    private fun onReadMoreClick() {
        var isExpended = false
        binding.tvSeeMore.setOnClickListener {
            if(!isExpended) {
                binding.tvTextCommentary.maxLines = 150
                isExpended = true
                binding.tvSeeMore.text = "Read Less.."
            } else {
                binding.tvTextCommentary.maxLines = 4
                isExpended = false
                binding.tvSeeMore.text = "Read More.."
            }
        }
    }

    private fun getAndSetChapterAndVerseNumber() {
        val bundle = arguments
        chapternumber = bundle?.getInt("chapterNumber")!!
        versenumber = bundle?.getInt("verseNumber")!!

        binding.tvVerseNumber.text = "||$chapternumber.$versenumber||"

    }

    private fun checkInternetConnectivity() {
        val networkManager = NetworkManager(requireContext())
        networkManager.observe(viewLifecycleOwner){
            if (it == true){
                binding.tvShowingMessage.visibility = View.GONE
                binding.ivNoInternet.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
                getVerseDetail()
            }
            else {
                binding.tvShowingMessage.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                binding.ivNoInternet.visibility = View.VISIBLE

                binding.tvVerseNumber.visibility = View.GONE
                binding.tvVerseText.visibility = View.GONE
                binding.tvTranslationIfEnglish.visibility = View.GONE
                binding.tvWordIfEnglish.visibility = View.GONE
                binding.view.visibility = View.GONE
                binding.tvTranslation.visibility = View.GONE
                binding.clTranslation.visibility = View.GONE
                binding.tvCommentries.visibility = View.GONE
                binding.clCommentries.visibility = View.GONE
                binding.backgroundImage.visibility = View.GONE
                binding.ivFavouriteVerse.visibility = View.GONE
            }
        }
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