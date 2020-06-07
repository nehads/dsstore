package com.nehads.friends.main.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.nehads.friends.R
import com.nehads.friends.di.factories.ViewModelProviderFactory
import com.nehads.friends.main.MainActivity
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.page_indicator_overlay.*
import javax.inject.Inject

class LoginFragment @Inject constructor() : DaggerFragment() {
   /* Inject viewModel and mainActivity instance */
    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory
    lateinit var loginVM: LoginViewModel

    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginVM = ViewModelProviders.of(this, viewModelProvider).get(LoginViewModel::class.java)

        /* Check if user is logged In to show login layout or proceed to landing page */
        loginVM.isUserLoggedIn()

        setClickListeners()
        activeListeners()
    }

    private fun setClickListeners() {
        btn_login.setOnClickListener {
            validateFields()
        }

        btn_verify_otp.setOnClickListener {
            loginVM.validateOTP(et_password.text.toString(), tl_password)
        }

        btn_skip.setOnClickListener {
            it.findNavController().navigate(R.id.toLandingFragment)
        }

        /* On text changed, hide the error */
        et_name.doOnTextChanged { _, _, _, _ ->
                tl_name.isErrorEnabled = false
        }

        et_email.doOnTextChanged { _, _, _, _ ->
            tl_email.isErrorEnabled = false
        }
    }

    private fun activeListeners() {
        loginVM.isLoading.observe(viewLifecycleOwner, Observer {
            loadGroup.visibility = if (it) View.VISIBLE else View.GONE
        })

        loginVM.pageNav.observe(viewLifecycleOwner, Observer {
            navigateToPage(it)
        })
    }

    private fun navigateToPage(it: Any?) {
        when (it) {
            1 -> { //when user name-email is entered and password field has to be shown
                cl_name.visibility = View.GONE
                cl_password.visibility = View.VISIBLE
            }
            2, true -> { //proceed to landing page
                Navigation.findNavController(btn_skip).navigate(R.id.toLandingFragment)
            }
        }
    }

    private fun validateFields() {
        loginVM.validateFields(et_name, et_email, tl_name, tl_email)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

}
