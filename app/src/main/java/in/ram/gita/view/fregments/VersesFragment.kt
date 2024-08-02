package `in`.ram.gita.view.fregments

import android.os.Bundle
import android.speech.tts.UtteranceProgressListener
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import `in`.ram.gita.NetworkManager
import `in`.ram.gita.R
import `in`.ram.gita.TextToSpeechManager
import `in`.ram.gita.databinding.FragmentVersesBinding
import `in`.ram.gita.view.adapter.AdapterVerses
import `in`.ram.gita.viewmodel.MainViewModel
import kotlinx.coroutines.launch


class VersesFragment : Fragment() {

    private lateinit var binding : FragmentVersesBinding
    private lateinit var adapterVerses: AdapterVerses
    private lateinit var textToSpeechManager: TextToSpeechManager
    private var chapternumber = 0
    private val viewModel : MainViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVersesBinding.inflate(layoutInflater)

        textToSpeechManager = TextToSpeechManager(requireActivity())
        onPlayClicked()
        onPauseClicked()
        setUtteranceProgressListener()
        changeStatusBarColor()
        onReadMoreClick()
        getAndSetChapterDetail()

        getData()
        return binding.root
    }

    private fun setUtteranceProgressListener() {
        textToSpeechManager.setOnUtteranceProgressListener(object : UtteranceProgressListener(){
            override fun onStart(utteranceId: String?) {
                activity?.runOnUiThread {
                    binding.progress.visibility = View.GONE
                    binding.ivPause.visibility = View.VISIBLE
                }
            }

            override fun onDone(utteranceId: String?) {
                activity?.runOnUiThread {
                    binding.ivPause.visibility = View.GONE
                    binding.ivPlay.visibility = View.VISIBLE
                    Toast.makeText(context, "Completed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onError(utteranceId: String?) {

            }

        })
    }

    private fun onPauseClicked() {
        binding.ivPause.setOnClickListener {
            binding.ivPause.visibility = View.GONE
            binding.ivPlay.visibility = View.VISIBLE
            textToSpeechManager.stop(false)
        }
    }

    private fun onPlayClicked() {
        binding.ivPlay.setOnClickListener {
            binding.progress.visibility = View.VISIBLE
            binding.ivPlay.visibility = View.GONE
            textToSpeechManager.speak(binding.tvChapterDescription.text.toString())
        }
    }

    private fun getData() {
        val bundle = arguments
        val showDataFromRoom = bundle?.getBoolean("showRoomData",false)

        if (showDataFromRoom == true){
            getDataFromRoom()
        }
        else{
            checkInternetConnectivity()
        }
    }

    private fun getDataFromRoom() {
        viewModel.getAParticularChapter(chapternumber).observe(viewLifecycleOwner){
            binding.tvChapterNumber.text = "Chapter ${it.chapterNumber}"
            binding.tvChapterTitle.text = it.nameTranslated
            binding.tvChapterDescription.text = it.chapterSummary
            binding.tvNumberOfVerses.text = it.versesCount.toString()

            showListInAdapter(it.verses,false)
        }
    }

    private fun onReadMoreClick() {
        var isExpended = false
        binding.tvSeeMore.setOnClickListener {
            if(!isExpended) {
                binding.tvChapterDescription.maxLines = 50
                isExpended = true
                binding.tvSeeMore.text = "Read Less.."
            } else {
                binding.tvChapterDescription.maxLines = 4
                isExpended = false
                binding.tvSeeMore.text = "Read More.."
            }
        }
    }

    private fun getAndSetChapterDetail() {
        val bundle = arguments
        chapternumber = bundle?.getInt("chapter_number")!!
        binding.tvChapterNumber.text = "Chapter ${bundle?.getInt("chapter_number")}"
        binding.tvChapterTitle.text = bundle?.getString("chapter_title")
        binding.tvChapterDescription.text = bundle?.getString("chapter_description")
        binding.tvNumberOfVerses.text = bundle?.getInt("verse_count").toString()

    }

    private fun getAllVerses() {
        lifecycleScope.launch {
            viewModel.getVerses(chapternumber).collect{

                val verseList = arrayListOf<String>()

                for (currentVerse in it) {
                    for (verses in currentVerse.translations!!){
                        if (verses != null) {
                            if (verses.language == "english"){
                                verses.description?.let { it1 -> verseList.add(it1) }
                                break
                            }
                        }
                    }
                }
                showListInAdapter(verseList,true)
            }
        }
    }

    private fun showListInAdapter(verseList: List<String>, onClick : Boolean) {
        adapterVerses = AdapterVerses(::onVerseItemVClicked , onClick)
        binding.rvVerse.adapter = adapterVerses
        adapterVerses.differ.submitList(verseList)
        binding.shimmer.visibility = View.GONE
    }

    private fun checkInternetConnectivity() {
        val networkManager = NetworkManager(requireContext())
        networkManager.observe(viewLifecycleOwner){
            if (it == true){
                binding.tvShowingMessage.visibility = View.GONE
                binding.shimmer.visibility = View.VISIBLE
                binding.rvVerse.visibility = View.VISIBLE
//                binding.ivNoInternet.visibility = View.GONE
                getAllVerses()
            }
            else {
                binding.shimmer.visibility = View.GONE
                binding.rvVerse.visibility = View.GONE
                binding.tvShowingMessage.visibility = View.VISIBLE
//                binding.ivNoInternet.visibility = View.VISIBLE
            }
        }
    }

    private fun onVerseItemVClicked(verse : String,verseNumber : Int){
        val bundle = Bundle()
        bundle.putInt("chapterNumber", chapternumber)
        bundle.putInt("verseNumber",verseNumber)

        findNavController().navigate(R.id.action_versesFragment_to_verseDetailFragment,bundle)
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
    override fun onDestroy() {
        super.onDestroy()
        textToSpeechManager.stop(true)
    }
}