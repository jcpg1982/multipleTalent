package pe.com.master.machines.multiplicatalent.ui.activities.mainActivity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import pe.com.master.machines.multiplicatalent.R
import pe.com.master.machines.multiplicatalent.Utils.Companion.recursivePopBackStack
import pe.com.master.machines.multiplicatalent.databinding.ActivityMainBinding
import pe.com.master.machines.multiplicatalent.ui.activities.base.BaseActivity
import pe.com.master.machines.multiplicatalent.ui.adapter.base.PagListener
import pe.com.master.machines.multiplicatalent.ui.fragments.homeFragment.HomeFragment


class MainActivity : BaseActivity(), SearchView.OnQueryTextListener {

    private val TAG = MainActivity::class.java.simpleName

    private lateinit var binding: ActivityMainBinding
    private var menuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setUpToolbar()
        if (savedInstanceState == null) {
            onAddFragmentToStack(
                null,
                HomeFragment.newInstance(),
                "Inicio",
                "",
                false,
                false,
                HomeFragment.TAG
            )
        }
        Log.v(TAG, "onCreate: ")
    }

    override fun onBackPressed() {
        if (getFragManager().recursivePopBackStack())
            return

        super.onBackPressed()
    }

    override fun getFragManager(): FragmentManager = supportFragmentManager

    override fun onAddFragmentToStack(
        fragmentOrigen: Fragment?,
        fragmentDestino: Fragment,
        title: CharSequence,
        subTitle: CharSequence?,
        addToBackStack: Boolean,
        isAnimated: Boolean,
        tag: String
    ) {
        Log.d(TAG, "onAddFragmentToStack")
        val ft = getFragManager().beginTransaction()

        ft.setBreadCrumbTitle(title)
        ft.setBreadCrumbShortTitle(subTitle)

        if (isAnimated) {
            ft.setCustomAnimations(
                R.anim.transition_slide_right_in,
                R.anim.transition_slide_left_out,
                android.R.anim.slide_in_left,
                R.anim.transition_slide_right_out
            )
        }

        if (fragmentOrigen != null) {
            if (fragmentDestino.isAdded) {
                ft.hide(fragmentOrigen)
                    .show(fragmentDestino)
            } else {
                ft.hide(fragmentOrigen)
                    .add(R.id.fragment_container, fragmentDestino, tag)
            }
        } else {
            if (fragmentDestino.isHidden) {
                ft.show(fragmentDestino)
            } else {
                ft.add(R.id.fragment_container, fragmentDestino, tag)
            }
        }

        if (addToBackStack)
            ft.addToBackStack(null)

        try {
            ft.commit()
        } catch (e: Exception) {
            ft.commitAllowingStateLoss()
            e.printStackTrace()
        }
    }

    override fun onRemoveFragmentToStack(withBackPressed: Boolean) {
        if (withBackPressed) {
            if (getFragManager().recursivePopBackStack()) return
            super.onBackPressed()
        } else {
            getFragManager().recursivePopBackStack()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        menuItem = menu.findItem(R.id.search)
        val searchView = MenuItemCompat.getActionView(menuItem) as SearchView
        searchView.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.includeToolbarMain.includeToolbarMain)
        setToolbarHomeAsUp(true)
        binding.includeToolbarMain.includeToolbarMain.setPadding(
            binding.includeToolbarMain.includeToolbarMain.paddingLeft,
            binding.includeToolbarMain.includeToolbarMain.paddingTop,
            binding.includeToolbarMain.includeToolbarMain.paddingRight,
            binding.includeToolbarMain.includeToolbarMain.paddingBottom
        )
    }

    private fun setToolbarHomeAsUp(isHome: Boolean) {
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true)
            actionBar.setDisplayShowTitleEnabled(false)
            actionBar.setDisplayHomeAsUpEnabled(!isHome)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Log.d(TAG, "onQueryTextChange newText: ${newText}")
        val fragmentHome = supportFragmentManager
            .findFragmentByTag(HomeFragment.TAG) as HomeFragment?
        Log.d(TAG, "onQueryTextChange fragmentHome: ${fragmentHome}")
        if (fragmentHome != null && fragmentHome.isVisible) {
            newText?.let {
                fragmentHome.searchMusic(it)
            }
        }
        return false
    }

    fun viewMenuToolbar(isVisible: Boolean) {
        if (isVisible)
            invalidateOptionsMenu()
        menuItem?.setVisible(isVisible)
    }
}