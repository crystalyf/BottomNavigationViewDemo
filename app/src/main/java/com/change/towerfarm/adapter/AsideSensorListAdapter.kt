package com.change.towerfarm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.change.towerfarm.R
import com.change.towerfarm.databinding.ItemAsideSensorBinding
import com.change.towerfarm.models.SensorInformation
import com.change.towerfarm.viewmodels.MainViewModel


class AsideSensorListAdapter(
    private var viewModel: MainViewModel?,
    private var list: MutableList<SensorInformation>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CustomLayoutHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_aside_sensor,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CustomLayoutHolder) {
            holder.bind(viewModel, position, list!!)
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    fun setDataList() {
        this.list = viewModel?.sensorInformationList?.value
        notifyDataSetChanged()
    }

    class CustomLayoutHolder(private val binding: ItemAsideSensorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            viewModel: MainViewModel?,
            position: Int,
            list: MutableList<SensorInformation>
        ) {
            binding.contactName.text = list[position].device
            binding.root.setOnClickListener {
                viewModel?.aSideItemClick(position)
            }
            binding.executePendingBindings()
        }
    }
}
