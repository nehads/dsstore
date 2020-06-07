package com.nehads.friends.main.history

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.nehads.friends.R
import com.nehads.friends.databinding.FragmentHistoryBinding
import com.nehads.friends.di.factories.ViewModelProviderFactory
import com.nehads.friends.main.MainActivity
import com.nehads.friends.main.landing.LandingViewModel
import com.nehads.friends.model.DTO.HistoryModel
import com.nehads.friends.util.addActionBar
import com.nehads.friends.util.showToast
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_history.*
import javax.inject.Inject

class HistoryFragment @Inject constructor() : DaggerFragment() {

    private var historyAdapter: HistoryListAdapter? = null

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory
    lateinit var landingVM: LandingViewModel

    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentHistoryBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_history, container, false
        )

        landingVM = ViewModelProviders.of(this, viewModelProviderFactory)
            .get(LandingViewModel::class.java)

        binding.historyViewModel = landingVM
        binding.lifecycleOwner = this.viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity.setSupportActionBar(toolbar)
        mainActivity.supportActionBar?.addActionBar(getString(R.string.history))

        historyAdapter = HistoryListAdapter { item -> itemClicked(item) }

        rv_history.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = historyAdapter
        }

        landingVM.addHistoryItems()

        activateListeners()

    }

    private fun activateListeners() {
        landingVM.historyList.observe(mainActivity, Observer {
            historyAdapter!!.updateList(it)
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    private fun itemClicked(item: HistoryModel) {
        mainActivity.showToast(getString(R.string.you_have_watched, item.date))
    }

}
