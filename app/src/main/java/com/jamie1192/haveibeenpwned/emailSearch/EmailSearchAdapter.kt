package com.jamie1192.haveibeenpwned.emailSearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jamie1192.haveibeenpwned.R
import com.jamie1192.haveibeenpwned.database.adapters.BreachedViewHolder
import com.jamie1192.haveibeenpwned.database.models.Breach
import io.reactivex.subjects.PublishSubject
import java.util.*

/**
 * Created by jamie1192 on 5/1/19.
 */
class EmailSearchAdapter : RecyclerView.Adapter<BreachedViewHolder>() {

    private var sitesList : List<Breach> = Collections.emptyList()
    private var onClickSite : PublishSubject<String> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreachedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.email_breach_list_item, parent, false)
        val vh = BreachedViewHolder(view)
        vh.itemView.setOnClickListener {
            onClickSite.onNext(vh.name.text.toString())
        }
        return vh
    }

    override fun getItemCount(): Int = sitesList.size

    override fun onBindViewHolder(holder: BreachedViewHolder, position: Int) {
        holder.name?.text = sitesList[position].name
        holder.data?.text = holder.setDataClasses(sitesList[position].dataClasses)
        holder.setSiteIcon(sitesList[position].logoPath)
    }

    fun setList(list : List<Breach>) {
        sitesList = list
        notifyDataSetChanged()
    }

    fun emptyList() {
        sitesList = kotlin.collections.emptyList()
        notifyDataSetChanged()
    }

    fun getClickedSite() : PublishSubject<String> = onClickSite

}