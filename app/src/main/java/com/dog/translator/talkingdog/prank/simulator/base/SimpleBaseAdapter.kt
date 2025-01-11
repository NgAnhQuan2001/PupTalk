package com.dog.translator.talkingdog.prank.simulator.base

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class SimpleBaseAdapter<T, VB : ViewBinding>(
    diffCallBack: DiffUtil.ItemCallback<T>
) : ListAdapter<T, SimpleBaseAdapter.BaseViewHolder<VB>>(
    diffCallBack
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VB> {
        val binding = createViewBinding(parent, viewType)
        changeItemSize(parent.context, binding)
        return BaseViewHolder(binding, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<VB>, position: Int) {
        bind(
            holder.binding,
            position,
            getItem(position),
            holder.viewType
        )
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<VB>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val payload = payloads.get(0) as? List<*>
        if (payload.isNullOrEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            bindPayload(
                holder.itemView.context,
                holder.binding,
                getItem(position),
                holder.viewType,
                payload
            )
        }
    }

    abstract fun createViewBinding(parent: ViewGroup, viewType: Int?): VB

    abstract fun bind(binding: VB, position: Int, data: T, viewType: Int)

    open fun bindPayload(
        context: Context,
        binding: VB,
        data: T,
        viewType: Int,
        payloads: List<*>
    ) {

    }

    open fun changeItemSize(context: Context, binding: VB) {

    }

    class BaseViewHolder<VB : ViewBinding>(val binding: VB, val viewType: Int) :
        RecyclerView.ViewHolder(binding.root)
}
