package com.nehads.friends.main.login

import android.app.Application
import android.os.Handler
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputLayout
import com.nehads.friends.R
import com.nehads.friends.di.repository.DataRepository
import com.nehads.friends.di.respository_helper.local.db.entity.User
import com.nehads.friends.util.isValidEmail
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class LoginViewModel @Inject
constructor(
    private var appDataRepository: DataRepository,
    private val mContext: Application,
    private val mCompositeDisposable: CompositeDisposable
) : ViewModel() {

    var isLoading = MutableLiveData<Boolean>()
    var pageNav = MutableLiveData<Any>()

    var name = MutableLiveData<String>()
    var email = MutableLiveData<String>()

    /* Check if name/email is blank or is meeting length criteria */
    fun validateFields(
        nameEt: EditText,
        emailEt: EditText,
        tl_name: TextInputLayout,
        tl_phone: TextInputLayout
    ) {
        val nameF = nameEt.text.toString()
        val emailF = emailEt.text.toString()

        var hasError = false

        if (nameF.isBlank()) {
            tl_name.isErrorEnabled = true
            tl_name.error = mContext.resources.getString(R.string.error_required)
            hasError = true
        } else if (nameF.length < 3) {
            tl_name.isErrorEnabled = true
            tl_name.error = mContext.resources.getString(R.string.error_name_short)
            hasError = true
        }

        if (emailF.isBlank()) {
            tl_phone.isErrorEnabled = true
            tl_phone.error = mContext.resources.getString(R.string.error_required)
            hasError = true
        } else if (!emailF.isValidEmail()) {
            tl_phone.isErrorEnabled = true
            tl_phone.error = mContext.resources.getString(R.string.error_invalid_email)
            hasError = true
        }

        if (!hasError)
            performLogin(nameF, emailF)
    }

    fun isUserLoggedIn() {
        pageNav.value = appDataRepository.getAutoLoginFlag()
    }

    /* To perform login, show a loader for a second and continue */
    private fun performLogin(nameF: String, emailF: String) {
        isLoading.value = true
        name.value = nameF
        email.value = emailF
        Handler().postDelayed({
            isLoading.value = false
            pageNav.value = 1
        }, 1000)
    }


    /* Check if password is blank or is less than 4 digits
    * If condition satisfied, save data -> show a loader for a second and trigger pageNav to proceed */
    fun validateOTP(pwd: String, tl_pwd: TextInputLayout) {
        isLoading.value = true
        when {
            pwd.isBlank() -> {
                isLoading.value = false
                tl_pwd.isErrorEnabled = true
                tl_pwd.error = mContext.resources.getString(R.string.error_required)
            }
            pwd.length < 4 -> {
                isLoading.value = false
                tl_pwd.isErrorEnabled = true
                tl_pwd.error = mContext.resources.getString(R.string.error_pwd_short)
            }
            else -> {
                appDataRepository.setAutoLoginFlag()
                appDataRepository.saveUserData(User(1, name.value!!, email.value!!))
                Handler().postDelayed({
                    isLoading.value = false
                    pageNav.value = 2
                }, 1000)
            }
        }
    }
}