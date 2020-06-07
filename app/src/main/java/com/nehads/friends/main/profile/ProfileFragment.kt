package com.nehads.friends.main.profile

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.nehads.friends.R
import com.nehads.friends.databinding.FragmentProfileBinding
import com.nehads.friends.di.factories.ViewModelProviderFactory
import com.nehads.friends.main.MainActivity
import com.nehads.friends.util.addActionBar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.page_indicator_overlay.*
import javax.inject.Inject

/* This Fragment is set data using dataBinding */
class ProfileFragment @Inject constructor() : DaggerFragment() {

    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory
    lateinit var profileVM: ProfileViewModel

    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        //bind to viewModel
        val binding: FragmentProfileBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile, container, false
        )

        profileVM = ViewModelProviders.of(this, viewModelProvider).get(ProfileViewModel::class.java)

        binding.profileTimeViewModel = profileVM
        binding.lifecycleOwner = this.viewLifecycleOwner

        /* create notification channel */
        createChannel(
            getString(R.string.friends_notification_channel_id),
            getString(R.string.friends_notification_channel_name)
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity.setSupportActionBar(toolbar)
        mainActivity.supportActionBar?.addActionBar(getString(R.string.profile))

        /* get user data */
        profileVM.getUserData()

        setClickListeners()
        activateListeners()
    }

    private fun setClickListeners() {

        btn_history.setOnClickListener {
            it.findNavController().navigate(R.id.toHistoryFragment)
        }

        tv_history.setOnClickListener {
            it.findNavController().navigate(R.id.toHistoryFragment)
        }

        btn_logout.setOnClickListener {
            showConfirmationDialog()
        }
    }

    private fun activateListeners() {
        profileVM.isLoading.observe(viewLifecycleOwner, Observer {
            loadGroup.visibility = if (it) View.VISIBLE else View.GONE
        })

        profileVM.errorData.observe(viewLifecycleOwner, Observer {
            Toast.makeText(mainActivity, it.toString(), Toast.LENGTH_SHORT).show()
        })

        profileVM.pageNav.observe(viewLifecycleOwner, Observer {
            navigateToPage(it)
        })
    }

    private fun navigateToPage(it: Any) {
        when (it) {
            1 -> {
                Navigation.findNavController(btn_logout).navigate(R.id.toLoginFragment)
            }
        }
    }

    private fun showConfirmationDialog() {
        val dialogBuilder = AlertDialog.Builder(mainActivity)
        dialogBuilder.apply {
            setMessage(getString(R.string.logout_confirmation))
            setPositiveButton(getString(R.string.option_yes)) { _, _ -> profileVM.logoutUser() }
            setNegativeButton(getString(R.string.option_cancel)) { dialog, _ -> dialog.cancel() }
            setCancelable(false)
        }.create().show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
                .apply {
                    setShowBadge(true)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.friends_notification_channel_description)

            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )
            notificationManager?.createNotificationChannel(notificationChannel)

        }
    }

}
