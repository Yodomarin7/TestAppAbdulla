package com.example.testappabdulla.screen

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.data.source.retrofit.CardRetrofit
import com.example.testappabdulla.App
import com.example.testappabdulla.MainActivity
import com.example.testappabdulla.R
import com.example.testappabdulla.viewmodel.CardInfoUiEvent
import com.example.testappabdulla.viewmodel.CardInfoViewModel
import com.example.testappabdulla.viewmodel.CardInfoViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class CardInfoFragment: Fragment(R.layout.fragment_card_info) {
    @Inject
    lateinit var cardInfoVMF: CardInfoViewModelFactory

    private lateinit var mainActivity: MainActivity
    private lateinit var cardInfoViewModel: CardInfoViewModel
    private lateinit var navController: NavController

    private val args: CardInfoFragmentArgs by navArgs()
    private lateinit var card: String

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)
        mainActivity = activity as MainActivity
        (mainActivity.applicationContext as App).appComponent.inject(this)
        cardInfoViewModel = ViewModelProvider(this, cardInfoVMF)[CardInfoViewModel::class.java]
        navController = findNavController()

        card = args.card
        initToolBar()
        initViews(v)
        cardInfoViewModel.reduce(CardInfoUiEvent.Get(card))

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                cardInfoViewModel.uiState.collect {
                    when {
                        it.showProgress -> { showProgress() }
                        it.error.isNotEmpty() -> { showError(it.error) }
                        it.data != null -> { showData(it.data) }
                        else -> { showError("Не правильный номер карты...") }
                    }
                }
            }
        }
    }

    private fun initToolBar() {
        mainActivity.toolBar.title = card
        mainActivity.toolBar.setNavigationIcon(R.drawable.ic_arrow_back_24)
        mainActivity.toolBar.setNavigationOnClickListener {
            navController.popBackStack()
        }
    }

    private lateinit var progressView: View
    private lateinit var errorView: View
    private lateinit var dataView: View

    private fun showProgress() {
        progressView.visibility = VISIBLE
        errorView.visibility = GONE
        dataView.visibility = GONE
    }

    private lateinit var errorTextView: TextView
    private lateinit var btnAgain:  Button
    private fun showError(errorTxt: String) {
        progressView.visibility = GONE
        errorView.visibility = VISIBLE
        dataView.visibility = GONE

        errorTextView.text = errorTxt
        btnAgain.setOnClickListener { cardInfoViewModel.reduce(CardInfoUiEvent.Get(card)) }
    }

    private lateinit var schemeTextView: TextView
    private lateinit var typeTextView: TextView
    private lateinit var brandTextView: TextView
    private lateinit var prepaidTextView: TextView
    private lateinit var lengthTextView: TextView
    private lateinit var luhnTextView: TextView
    private lateinit var countryTextView: TextView
    private lateinit var locationTextView: TextView
    private lateinit var bankTextView: TextView
    private fun showData(card: CardRetrofit.CardInfoModel) {
        progressView.visibility = GONE
        errorView.visibility = GONE
        dataView.visibility = VISIBLE

        schemeTextView.text = card.scheme ?: "?"
        typeTextView.text = card.type ?: "?"
        brandTextView.text = card.brand ?: "?"
        when(card.prepaid) {
            true -> { prepaidTextView.text = "YES" }
            false-> { prepaidTextView.text = "NO" }
            else -> { prepaidTextView.text = "?" }
        }
        lengthTextView.text = "${card.number?.length}"
        when(card.number?.luhn) {
            true -> { luhnTextView.text = "YES" }
            false -> { luhnTextView.text = "NO" }
            else -> { luhnTextView.text = "?" }
        }
        countryTextView.text = "${card.country?.emoji ?: "?"} ${card.country?.name ?: "?"}"

        val locStr: String = if(card.country?.latitude == null || card.country?.longitude == null) "?"
        else { "<a href=\"geo:${card.country!!.latitude}, ${card.country!!.longitude}\"> " +
                "latitude: ${card.country?.latitude}, longitude: ${card.country?.longitude } </a>" }

            "latitude: ${card.country?.latitude ?: "?"}, " +
                "longitude: ${card.country?.longitude ?: "?"}"

        locationTextView.text = Html.fromHtml(locStr)

        val urlStr: String = if(card.bank?.url == null) "?"
        else { "<a href=\"https://${card.bank!!.url}\"> ${card.bank!!.url} </a>" }

        val phoneStr: String = if(card.bank?.phone == null) "?"
        else { "<a href=\"tel:${card.bank!!.phone}\"> ${card.bank!!.phone} </a>" }

        bankTextView.text = Html.fromHtml("${card.bank?.name ?: "?"}, ${card.bank?.city ?: "?"} " +
                "<br><br>" + urlStr + "<br><br>" + phoneStr)
    }

    private fun initViews(v: View) {
        progressView = v.findViewById(R.id.progress)
        errorView = v.findViewById(R.id.error)
        dataView = v.findViewById(R.id.data)

        errorTextView = v.findViewById(R.id.errorTxt)
        btnAgain = v.findViewById(R.id.againBtn)

        schemeTextView = v.findViewById(R.id.scheme)
        typeTextView = v.findViewById(R.id.type)
        brandTextView = v.findViewById(R.id.brand)
        prepaidTextView = v.findViewById(R.id.prepaid)
        lengthTextView = v.findViewById(R.id.length)
        luhnTextView = v.findViewById(R.id.luhn)
        countryTextView = v.findViewById(R.id.country)
        locationTextView = v.findViewById(R.id.location)
        bankTextView = v.findViewById(R.id.bank)

        bankTextView.movementMethod = LinkMovementMethod.getInstance()
        locationTextView.movementMethod = LinkMovementMethod.getInstance()
    }
}














