package com.change.towerfarm.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.change.towerfarm.base.BaseFragment
import com.change.towerfarm.databinding.FragmentConsultBinding
import com.change.towerfarm.databinding.FragmentRegisterBinding
import com.change.towerfarm.extensions.getViewModelFactory
import com.change.towerfarm.viewmodels.MainViewModel

class RegisterFragment : BaseFragment() {

    private val viewModel by activityViewModels<MainViewModel> { getViewModelFactory() }
    private var dataBinding: FragmentRegisterBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentRegisterBinding.inflate(inflater, container, false)
        dataBinding?.viewModel = viewModel
        return dataBinding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataBinding?.lifecycleOwner = this.viewLifecycleOwner
        observeApiErrorEvent(viewModel)
        observeApiLoadingEvent(viewModel)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dataBinding = null
    }

}