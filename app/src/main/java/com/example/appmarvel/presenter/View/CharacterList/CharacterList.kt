package com.example.appmarvel.presenter.View.CharacterList

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appmarvel.R
import com.example.appmarvel.databinding.FragmentCharacterListBinding
import com.example.appmarvel.presenter.Adapter.CharacterAdapter
import com.example.appmarvel.presenter.View.CharacterViewModel.CharacterListViewModel
import com.example.appmarvel.util.ResourceState
import com.example.appmarvel.util.hide
import com.example.appmarvel.util.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CharacterList : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var searchTerm: String
    private val viewModel: CharacterListViewModel by viewModels()
    private lateinit var binding: FragmentCharacterListBinding
    private val characterAdapter by lazy { CharacterAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCharacterListBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectObserver()
        setupRecyclerView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun setupRecyclerView() = with(binding) {
        recyclerView.apply {
            adapter = characterAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            viewModel
        }
    }

    private fun collectObserver() = lifecycleScope.launch {
        viewModel.characterList.collect { resource ->
            when (resource) {
                is ResourceState.Success -> {
                    resource.data?.let { values ->
                        binding.progressCircular.hide()
                        characterAdapter.characters = values.data.results.toList()
                    }

                }

                is ResourceState.Error -> {
                    binding.progressCircular.hide()

                    resource.message
                }

                is ResourceState.Load -> {
                    binding.progressCircular.show()
                }

                else -> {}
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menu, menu)
        val search = menu.findItem(R.id.searchMenu)
        val searchView = search?.actionView as androidx.appcompat.widget.SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchTerm = query
        }
        if (searchTerm.isNotEmpty()) {
            search()
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchTerm = newText
        }
        if (searchTerm.isNotEmpty()) {
            search()
        }
        return true
    }

    private fun search() {
        viewModel.getSearchedCharacters(searchTerm)
    }


}