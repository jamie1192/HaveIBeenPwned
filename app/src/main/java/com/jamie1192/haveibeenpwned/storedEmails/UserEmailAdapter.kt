package com.jamie1192.haveibeenpwned.storedEmails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.jamie1192.haveibeenpwned.R
import com.jamie1192.haveibeenpwned.database.models.UserEmail
import kotlinx.android.synthetic.main.user_email_list_item.view.*
import java.util.*

/**
 * Created by jamie1192 on 13/1/19.
 */
class UserEmailAdapter : RecyclerView.Adapter<UserEmailAdapter.EmailViewHolder>() {

    private var emailList : List<UserEmail> = Collections.emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailViewHolder {
        return EmailViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.user_email_list_item, parent, false))
    }

    override fun getItemCount(): Int = emailList.size

    override fun onBindViewHolder(holder: EmailViewHolder, position: Int) {
        holder.email.text = emailList[position].email
    }

    fun setList(list: List<UserEmail>) {
        emailList = list
        notifyDataSetChanged()
    }

    //TODO use this to implement undo snackbar
//    fun removeRow(pos : Int) {
//        emailList.removeAt(pos)
//        notifyItemRemoved(pos)
//    }

    /** ViewHolder **/

    class EmailViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val email: TextView = itemView.user_email_textview
        val foreground: MaterialCardView = itemView.row_foreground
        val background: RelativeLayout = itemView.row_background
    }

}