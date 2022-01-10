package com.change.towerfarm.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.change.towerfarm.activity.MainActivity
import com.change.towerfarm.base.BaseFragment
import com.change.towerfarm.databinding.FragmentRecordBinding
import com.change.towerfarm.extensions.getViewModelFactory
import com.change.towerfarm.viewmodels.MainViewModel

class RecordFragment : BaseFragment() {

    private val viewModel by activityViewModels<MainViewModel> { getViewModelFactory() }
    private var dataBinding: FragmentRecordBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentRecordBinding.inflate(inflater, container, false)
        dataBinding?.viewModel = viewModel
        return dataBinding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataBinding?.lifecycleOwner = this.viewLifecycleOwner
        observeApiErrorEvent(viewModel)
        observeApiLoadingEvent(viewModel)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).getToolbar()?.title = "操作履历"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dataBinding = null
    }

}