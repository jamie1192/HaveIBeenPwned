package com.jamie1192.haveibeenpwned

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import co.zsmb.materialdrawerkt.builders.accountHeader
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import com.jamie1192.haveibeenpwned.breachedSites.BreachesFragment
import com.jamie1192.haveibeenpwned.emailSearch.EmailSearchFragment
import com.jamie1192.haveibeenpwned.storedEmails.UserEmailsFragment
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic
import com.mikepenz.materialdrawer.Drawer
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.reflect.KClass

class MainActivity : AppCompatActivity() {


    private lateinit var drawer : Drawer
    private val compDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(main_toolbar)

        getDrawer()
        setFragment(BreachesFragment.newInstance("one", "two"))
    }



    override fun onDestroy() {
        compDisposable.dispose()
        super.onDestroy()
    }

    private fun getDrawer() {
        drawer = drawer {
            accountHeader {

            }
            toolbar = this@MainActivity.main_toolbar
            primaryItem("Home") {
                iicon = MaterialDesignIconic.Icon.gmi_home
                onClick { _ ->
                    setFragment(BreachesFragment.newInstance("nil", "nil"))
                    false
                }
            }
            primaryItem("pwned?") {
                iicon = MaterialDesignIconic.Icon.gmi_search
                onClick { _ ->
                    setFragment(EmailSearchFragment.newInstance())
                    false
                }
            }
            primaryItem("My Email Addresses") {
                iicon = MaterialDesignIconic.Icon.gmi_email
                onClick { _->
                    setFragment(UserEmailsFragment.newInstance())
                    false
                }
            }
        }
    }




    private fun <T : Activity> openActivity(activity: KClass<T>): (View?) -> Boolean = {
        startActivity(Intent(this@MainActivity, activity.java))
        false
    }

    private fun setFragment(fragment : Fragment) {
        val ft = supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_activity_main, fragment, fragment::class.simpleName)
//            .commit()
        ft.commit()
    }

}
