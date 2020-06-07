package com.nehads.friends.main.episodes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.nehads.friends.R
import com.nehads.friends.databinding.FragmentEpisodeListingBinding
import com.nehads.friends.di.factories.ViewModelProviderFactory
import com.nehads.friends.main.MainActivity
import com.nehads.friends.main.landing.LandingViewModel
import com.nehads.friends.util.showToast
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_episode_listing.*
import kotlinx.android.synthetic.main.page_indicator_overlay.*
import javax.inject.Inject

class EpisodesFragment @Inject constructor() : DaggerFragment() {
    /* Inject viewModel and activity to get instance */
    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory
    lateinit var landingVM: LandingViewModel

    lateinit var mainActivity: MainActivity
    private var listingAdapter: EpisodeListingAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentEpisodeListingBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_episode_listing, container, false
        )

        landingVM = ViewModelProviders.of(this, viewModelProviderFactory)
            .get(LandingViewModel::class.java)

        binding.episodesViewModel = landingVM
        binding.lifecycleOwner = this.viewLifecycleOwner


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Fetch seasonNo, In case season no is null, it will take 0 which is 'specials'
        * send season number to get episodes for clicked season*/
        val seasonNo = arguments?.getInt("season_no") ?: 0
        landingVM.getEpisodes(seasonNo)

        /* Initialize adapter */
        listingAdapter =
            EpisodeListingAdapter()

        rv_details_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listingAdapter
        }

        /* Listen for data from viewModel */
        activateListeners()
    }

    private fun activateListeners() {
        landingVM.episodesList.observe(viewLifecycleOwner, Observer {
            listingAdapter?.updateList(it.first,it.second)
        })

        landingVM.errorData.observe(viewLifecycleOwner, Observer {
            mainActivity.showToast(it ?: "")
        })

        /* Show progress loader */
        landingVM.isLoading.observe(viewLifecycleOwner, Observer {
            loadGroup.isVisible = it
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

}
