package com.change.towerfarm.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.change.towerfarm.base.BaseFragment
import com.change.towerfarm.databinding.FragmentConsultBinding
import com.change.towerfarm.extensions.getViewModelFactory
import com.change.towerfarm.viewmodels.MainViewModel
import androidx.navigation.fragment.findNavController
import com.change.towerfarm.R
import com.change.towerfarm.databinding.FragmentLoginBinding
import com.change.towerfarm.utils.EventObserver
import com.change.towerfarm.viewmodels.StartViewModel
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.auth.FirebaseAuthWebException

class LoginFragment : BaseFragment() {

    companion object {

        /**
         * 指定した画面を表示する
         * 0：Main、 1：註冊、 2：forget password
         */
        enum class ToView {
            TO_MAIN,
            TO_REGISTER,
            TO_FORGET_PWD
        }
    }

    private val viewModel by activityViewModels<StartViewModel> { getViewModelFactory() }
    private var dataBinding: FragmentLoginBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentLoginBinding.inflate(inflater, container, false)
        dataBinding?.viewModel = viewModel
        initView()
        return dataBinding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataBinding?.lifecycleOwner = this.viewLifecycleOwner
        observeApiErrorEvent(viewModel)
        observeApiLoadingEvent(viewModel)
    }

    private fun initView(){
        if (!preferences.getHasLogin()) {
            preferences.putHasLogin(true)
            toNextView(ToView.TO_REGISTER)
        }

        dataBinding?.btnFacebookSignIn?.setPermissions("email")
        dataBinding?.btnFacebookSignIn?.fragment = this
        dataBinding?.btnFacebookSignIn?.registerCallback(viewModel.callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                viewModel.handleFacebookAccessToken(activity,loginResult.accessToken)
            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException) {
            }
        })

        viewModel.loginButtonClick .observe(viewLifecycleOwner, EventObserver {
            //todo: login
            toNextView(ToView.TO_MAIN)
        })

        viewModel.registerButtonClick .observe(viewLifecycleOwner, EventObserver {
            //todo: register
        })

        viewModel.googleSignInClick .observe(viewLifecycleOwner, EventObserver {
            viewModel.doGoogleAuthentication(activity)
        })

        viewModel.firebaseException.observe(viewLifecycleOwner, EventObserver {
            if (!(it is FirebaseAuthWebException && it.errorCode == "ERROR_WEB_CONTEXT_CANCELED")) {
              Toast.makeText(context,"firebase exception",Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * 指定した画面を表示する
     */
    private fun toNextView(toView: ToView) {
        when (toView) {
            ToView.TO_REGISTER -> {
                findNavController().navigate(
                   R.id.action_loginFragment_to_registerFragment
                )
            }
            ToView.TO_FORGET_PWD -> {
                findNavController().navigate(
                    R.id.action_loginFragment_to_forgetPwdFragment
                )
            }
            ToView.TO_MAIN -> {
                findNavController().navigate(
                    R.id.action_loginFragment_to_mainActivity
                )
                activity?.finish()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dataBinding = null
    }

}