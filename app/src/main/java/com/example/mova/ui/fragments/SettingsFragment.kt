package com.example.mova.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mova.databinding.FragmentSettingsBinding
import com.google.android.material.snackbar.Snackbar

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        val arrayAdapter: ArrayAdapter<*>
        val users = arrayOf("Saved", "Quotes", "About Us")

        // access the listView from xml file
        arrayAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1, users
        )
        binding.listView.adapter = arrayAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.listView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                when (position) {
                    0 -> findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToSavedFragment())
                    1 -> findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToQuotesFragment())
                    3 -> Snackbar.make(
                        requireView(),
                        "mohamed.ayad7474@gmail.com",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }

    }
}

