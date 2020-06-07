package com.nehads.friends.main.landing

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.nehads.friends.R
import com.nehads.friends.di.factories.ViewModelProviderFactory
import com.nehads.friends.main.MainActivity
import com.nehads.friends.util.*
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_landing.*
import kotlinx.android.synthetic.main.page_indicator_overlay.*
import javax.inject.Inject

class LandingFragment @Inject constructor() : DaggerFragment() {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory
    lateinit var landingVM: LandingViewModel

    lateinit var mainActivity: MainActivity

    private var listingAdapter: ListingAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.fragment_landing, container, false)

        landingVM = ViewModelProviders.of(this, viewModelProviderFactory)
            .get(LandingViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Update toolbar title */
        mainActivity.setSupportActionBar(toolbar)
        mainActivity.supportActionBar?.addActionBar(getString(R.string.splash_title))

        listingAdapter = ListingAdapter()

        landingVM.getSeasons()

        /* recycler view with span of 2 i.e. 2 columns */
        rv_listing.apply {
            layoutManager = GridLayoutManager(context, 2)
            addItemDecoration(SpacesItemDecoration(2, 24, true))
            adapter = listingAdapter
        }

        setClickListeners()
        activateListeners()
    }

    private fun activateListeners() {
        landingVM.seasonsList.observe(viewLifecycleOwner, Observer {
            listingAdapter?.updateList(it)
        })

        /* Load data into 'Specials' season views */
        landingVM.specials.observe(viewLifecycleOwner, Observer {
            iv_specials.clipToOutline = true
            iv_specials.load("${Constants.IMAGE_BASE_URL}${it.posterPath}")
            grp_specials.isVisible = true
        })

        landingVM.errorData.observe(viewLifecycleOwner, Observer {
            mainActivity.showToast(it ?: "")
        })

        landingVM.isLoading.observe(viewLifecycleOwner, Observer {
            loadGroup.isVisible = it
        })
    }

    private fun setClickListeners() {
        iv_specials.setOnClickListener {
            it.findNavController().navigate(R.id.toEpisodesFragment)
        }

        iv_settings.setOnClickListener {
            it.findNavController().navigate(R.id.toProfileFragment)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

}
