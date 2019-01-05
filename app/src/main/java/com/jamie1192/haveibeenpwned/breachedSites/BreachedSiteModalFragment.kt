package com.jamie1192.haveibeenpwned.breachedSites

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jamie1192.haveibeenpwned.R
import com.jamie1192.haveibeenpwned.database.models.Breach
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_breachedsitemodal.*
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val key = "ARG_SITE_NAME"

class BreachedSiteModalFragment : RoundedBottomSheet() {

    private lateinit var siteName : String
    private lateinit var viewModel: BreachedSitesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        siteName = arguments?.getString(key).toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_breachedsitemodal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(BreachedSitesViewModel::class.java)
        loadSelectedSite()

    }

    private fun loadSelectedSite() {
        viewModel.getSiteDetails(siteName).observe(this, Observer {
            setSiteDetails(it)
        })
    }

    private fun setSiteDetails(breach : Breach) {
        val text = "${NumberFormat.getNumberInstance(Locale.getDefault()).format(breach.pwnCount)} accounts pwned"

        modal_site_pwn_count.text = text
        modal_site_name.text = breach.name
        modal_site_breach_date.text = setBreachDate(breach.breachDate)

        val zdt = ZonedDateTime.parse(breach.addedDate).format(DateTimeFormatter.ofPattern("d MMM yyyy", Locale.getDefault()))
        modal_site_added_date.text = zdt.toString()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            modal_site_details.text = Html.fromHtml(breach.description, Html.FROM_HTML_MODE_LEGACY)
        } else {
            modal_site_details.text = Html.fromHtml(breach.description)
        }
        //
        modal_site_details.movementMethod = LinkMovementMethod.getInstance()

        Picasso.get()
            .load(breach.logoPath)
            .into(modal_site_icon)
    }

    private fun setBreachDate(date : String?) : String {

        val inputDf = SimpleDateFormat("yyyy-MM-dd")
        val outputDf = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
        val newDate = inputDf.parse(date)
        return outputDf.format(newDate)
    }


    companion object {

        fun newInstance(name: String): BreachedSiteModalFragment =
            BreachedSiteModalFragment().apply {
                arguments = Bundle().apply {
                    putString(key, name)
                }
            }

    }
}
