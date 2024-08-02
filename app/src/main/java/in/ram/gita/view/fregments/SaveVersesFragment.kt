package `in`.ram.gita.view.fregments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import `in`.ram.gita.R
import `in`.ram.gita.databinding.FragmentSaveVersesBinding
import `in`.ram.gita.datasource.api.room.SavedVerses
import `in`.ram.gita.view.adapter.AdapterSavedVerses
import `in`.ram.gita.viewmodel.MainViewModel


class SaveVersesFragment : Fragment() {
    private lateinit var binding: FragmentSaveVersesBinding
    private lateinit var adapterSavedVerses: AdapterSavedVerses
    private val viewModel : MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSaveVersesBinding.inflate(layoutInflater)
        getVersesFromRoom()
        return binding.root
    }

    private fun getVersesFromRoom() {
        viewModel.getAllEnglishVerse().observe(viewLifecycleOwner){
            val verseList = arrayListOf<String>()

            adapterSavedVerses = AdapterSavedVerses(::onVerseItemVClicked)
            binding.rvVerses.adapter = adapterSavedVerses
            adapterSavedVerses.differ.submitList(it)
            binding.shimmer.visibility = View.GONE

        }
    }

    fun onVerseItemVClicked(verse : SavedVerses) {

        val bundle = Bundle()

        bundle.putBoolean("showRoomData",true)
        bundle.putInt("chapterNumber", verse.chapterNumber!!)
        bundle.putInt("verseNumber", verse.verseNumber!!)

        findNavController().navigate(R.id.action_saveVersesFragment_to_verseDetailFragment,bundle)
    }
}