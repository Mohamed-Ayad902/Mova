package com.example.mova.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mova.R
import com.example.mova.adapters.callbacks.OnQuoteClickListener
import com.example.mova.adapters.QuotesAdapter
import com.example.mova.data.quote.Quote
import com.example.mova.databinding.FragmentQuotesBinding

class QuotesFragment : Fragment() {

    private lateinit var binding: FragmentQuotesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuotesBinding.inflate(layoutInflater, container, false)
        binding.tvRandom.setOnClickListener {
            findNavController().navigate(
                QuotesFragmentDirections.actionQuotesFragmentToQuoteDetailsFragment(null)
            )
        }

        val quotesAdapter = QuotesAdapter(object : OnQuoteClickListener {
            override fun onClick(quote: Quote) {
                findNavController().navigate(
                    QuotesFragmentDirections.actionQuotesFragmentToQuoteDetailsFragment(quote.slug)
                )
            }
        }, requireContext())
        val quotes = listOf(
            Quote(R.drawable.mindhunter, "Mind Hunter", "mindhunter"),
            Quote(R.drawable.true_detective, "True Detective", "true-detective"),
            Quote(R.drawable.soprano, "Soprano", "soprano"),
            Quote(R.drawable.the_wire, "The Wire", "the-wire"),
            Quote(R.drawable.sillicon_valley, "Sillicon Valley", "sillicon-valley"),
            Quote(R.drawable.the_office, "The Office", "the-office"),
            Quote(R.drawable.space_force, "Space Force", "space-force"),
            Quote(R.drawable.fargo, "Fargo", "fargo"),
            Quote(R.drawable.ozark, "Ozark", "ozark"),
            Quote(R.drawable.lucifer, "Lucifer", "lucifer"),
            Quote(R.drawable.american_psycho, "American Psycho", "american-psycho"),
            Quote(R.drawable.the_machinist, "The Machinist", "the-machinist"),
            Quote(R.drawable.god_father, "God Father", "god-father"),
            Quote(
                R.drawable.the_silence_of_the_lambs,
                "The Silence Of The Lambs",
                "the-silence-of-the-lambs"
            ),
            Quote(R.drawable.forrest_gump, "Forrest Gump", "forrest-gump"),
            Quote(
                R.drawable.spiral_from_the_book_of_saw,
                "Spiral From The Book Of Saw",
                "spiral-from-the-book-of-saw"
            )
        )
        quotesAdapter.differ.submitList(quotes)

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = quotesAdapter
        }
        return binding.root
    }


}