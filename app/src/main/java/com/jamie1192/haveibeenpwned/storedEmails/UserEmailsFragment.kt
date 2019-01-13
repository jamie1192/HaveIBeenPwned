package com.jamie1192.haveibeenpwned.storedEmails

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

import com.jamie1192.haveibeenpwned.R
import com.jamie1192.haveibeenpwned.utils.RecyclerSwipeHelper
import kotlinx.android.synthetic.main.user_emails_fragment.*

class UserEmailsFragment : Fragment(), RecyclerSwipeHelper.SwipeListener {

    private val adapter = UserEmailAdapter()

    companion object {
        fun newInstance() = UserEmailsFragment()
    }

    private lateinit var viewModel: UserEmailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_emails_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(UserEmailsViewModel::class.java)

        getAllEmails()
    }

    private fun setupUI() {
        user_emails_recycler_view.layoutManager = LinearLayoutManager(context)
        user_emails_recycler_view.adapter = adapter
        val simpleCallback = RecyclerSwipeHelper(0, ItemTouchHelper.LEFT, this)
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(user_emails_recycler_view)

        addEmailButton.setOnClickListener {
            insert_email_input.text?.trim()?.let { text ->
                viewModel.insertEmail(text.toString())
                insert_email_input.text!!.clear()
            }
        }

        insert_email_input.setOnEditorActionListener{ _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE
                    && !insert_email_input.text.isNullOrEmpty()
            ) {
                viewModel.insertEmail(insert_email_input.text.toString())
                insert_email_input.text!!.clear()
            }
            false
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, dir: Int, pos: Int) {
        val evh = viewHolder as UserEmailAdapter.EmailViewHolder
        viewModel.deleteEmail(evh.email.text.toString())
    }

    private fun getAllEmails() {
        viewModel.getAllEmails().observe(this, Observer {
            adapter.setList(it)
        })
    }

    private fun showSnackbar() {
        view?.let {
            var snackbar = Snackbar.make(it, "Email deleted!", Snackbar.LENGTH_LONG)
                .setAction("undo") { undoDelete() }
                .show()
            
        }
    }

    private fun undoDelete() {

    }
}
