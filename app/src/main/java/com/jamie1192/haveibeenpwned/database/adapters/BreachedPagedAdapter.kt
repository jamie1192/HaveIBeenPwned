package com.jamie1192.haveibeenpwned.database.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.jamie1192.haveibeenpwned.R
import com.jamie1192.haveibeenpwned.database.models.Breach
import io.reactivex.subjects.PublishSubject

/**
 * Created by jamie1192 on 24/12/18.
 */
class BreachedPagedAdapter : PagedListAdapter<Breach, BreachedViewHolder>(DIFF_CALLBACK) {

    private var onClickSite : PublishSubject<String> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreachedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.breached_site_list_item, parent, false)
        val viewholder = BreachedViewHolder(view)
        viewholder.itemView.setOnClickListener {
            onClickSite.onNext(viewholder.name.text.toString())
        }
        return viewholder
    }

    override fun onBindViewHolder(holder: BreachedViewHolder, position: Int) {

        val breach = getItem(position)
        if (breach != null) {
            holder.name?.text = breach.name
            holder.data?.text = holder.setDataClasses(breach.dataClasses)
            holder.setSiteIcon(breach.logoPath)
        }
        else {
            holder.clear()
        }
    }

    fun getOnClickSite() : PublishSubject<String> = onClickSite

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Breach>() {
            override fun areItemsTheSame(oldItem: Breach, newItem: Breach): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: Breach, newItem: Breach): Boolean = oldItem == newItem

        }
    }
}