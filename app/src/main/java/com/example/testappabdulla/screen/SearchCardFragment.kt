package com.example.testappabdulla.screen

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.data.source.room.CardEntity
import com.example.testappabdulla.App
import com.example.testappabdulla.MainActivity
import com.example.testappabdulla.R
import com.example.testappabdulla.adapter.SearchCardAdapter
import com.example.testappabdulla.extention.hideKeyboard
import com.example.testappabdulla.viewmodel.SearchCardViewModel
import com.example.testappabdulla.viewmodel.SearchCardViewModelFactory
import com.example.testappabdulla.viewmodel.SearchCardUiEvent
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchCardFragment: Fragment(R.layout.fragment_search_card) {
    @Inject
    lateinit var searchCardVMF: SearchCardViewModelFactory

    private lateinit var mainActivity: MainActivity
    private lateinit var searchCardViewModel: SearchCardViewModel
    private lateinit var navController: NavController

    private lateinit var recyclerViewAdapter: SearchCardAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = activity as MainActivity
        (mainActivity.applicationContext as App).appComponent.inject(this)
        searchCardViewModel = ViewModelProvider(this, searchCardVMF)[SearchCardViewModel::class.java]
        navController = findNavController()

        initToolBar()
        initRecyclerView(view)
        initTextField(view)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchCardViewModel.uiState.collect {
                    recyclerViewAdapter.submitList(it.data)
                }
            }
        }
    }

    private fun initRecyclerView(v: View) {
        val recyclerView = v.findViewById<RecyclerView>(R.id.recycler_view)

        val lm = LinearLayoutManager(mainActivity)
        recyclerView.layoutManager = lm
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, lm.orientation))

        recyclerViewAdapter = SearchCardAdapter(navController, searchCardViewModel)
        recyclerView.adapter = recyclerViewAdapter

        val itemTouchHelper = ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                recyclerViewAdapter.onSwiped(viewHolder.adapterPosition)
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun initTextField(v: View) {
        val textSearch = v.findViewById<TextInputLayout>(R.id.textSearch)

        textSearch?.editText?.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    //textSearch.editText?.setText("")
                    //val regexTxt = v.text.toString().replace(Regex("[^0-9]"), "")

                    val card = v.text.toString()
                    if(card.isNotEmpty()) {
                        searchCardViewModel.reduce(SearchCardUiEvent.Create(
                            CardEntity(
                                uid = 0,
                                card = card)
                        ))
                        val action = SearchCardFragmentDirections.actionSearchCardFragmentToCardInfoFragment(card)
                        navController.navigate(action)
                    }

                    hideKeyboard()
                    v.clearFocus()
                    true
                }
                else -> false
            }
        }
    }

    private fun initToolBar() {
        mainActivity.toolBar.title = resources.getString(R.string.app_name)
        mainActivity.toolBar.navigationIcon = null
        mainActivity.toolBar.setNavigationOnClickListener(null)
    }
}