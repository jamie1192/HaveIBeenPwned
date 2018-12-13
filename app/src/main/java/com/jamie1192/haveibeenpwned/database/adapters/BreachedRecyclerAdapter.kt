package com.jamie1192.haveibeenpwned.database.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jamie1192.haveibeenpwned.R
import com.jamie1192.haveibeenpwned.api.models.Site
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.breached_site_list_item.view.*

/**
 * Created by jamie1192 on 9/12/18.
 */
class BreachedRecyclerAdapter : RecyclerView.Adapter<BreachedViewHolder>() {

    private var sitesList : List<Site>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreachedViewHolder {
        return BreachedViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.breached_site_list_item, parent, false))
    }



    override fun getItemCount(): Int {
        return sitesList?.size ?: 0
    }

    override fun onBindViewHolder(holder: BreachedViewHolder, position: Int) {
        holder.name?.text = sitesList?.get(position)?.name
        holder.data?.text = holder.setDataClasses(sitesList?.get(position)?.dataclasses)
        holder.setSiteIcon(sitesList?.get(position)?.logoPath)
    }

    fun setList(list: List<Site>?) {
        sitesList = list!!
        notifyDataSetChanged()
    }
}

/** Viewholder **/

class BreachedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val name = itemView.site_name
    val data = itemView.site_data
    val icon = itemView.site_icon

    fun setSiteIcon(url: String?) {
        Picasso.get()
            .load(url)
            //TODO placeholder
            .into(icon)
    }

    fun setDataClasses(data: List<String>?) : String {
        var text = ""
        data?.forEach {
            text += if ( it == data.last()) {
                it
            } else ("$it, ")
        }
        return text
    }
}