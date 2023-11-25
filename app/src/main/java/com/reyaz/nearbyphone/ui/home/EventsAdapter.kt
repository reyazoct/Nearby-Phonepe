package com.reyaz.nearbyphone.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.reyaz.nearbyphone.data.Event
import com.reyaz.nearbyphone.databinding.EventItemBinding

class EventsAdapter : PagingDataAdapter<Event, EventsAdapter.EventViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = EventItemBinding.inflate(inflater)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)
    }

    inner class EventViewHolder(private val binding: EventItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Event) {
            binding.event = item
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Event,
            newItem: Event
        ) = oldItem == newItem

        override fun getChangePayload(oldItem: Event, newItem: Event): Any? {
            if (oldItem != newItem) {
                return newItem
            }

            return super.getChangePayload(oldItem, newItem)
        }
    }
}