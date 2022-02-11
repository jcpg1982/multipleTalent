package pe.com.master.machines.multiplicatalent.ui.activities.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import pe.com.master.machines.multiplicatalent.di.dependencies.AppComponent
import pe.com.master.machines.multiplicatalent.ui.Application

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun getAppComponent(): AppComponent {
        return (application as Application).getAppComponent()
    }

    fun cleanAppComponent() {
        (application as Application).clearDaggerComponent()
    }

    abstract fun getFragManager(): FragmentManager

    abstract fun onAddFragmentToStack(
        fragmentOrigen: Fragment?,
        fragmentDestino: Fragment,
        title: CharSequence,
        subTitle: CharSequence?,
        addToBackStack: Boolean,
        isAnimated: Boolean,
        tag: String
    )

    abstract fun onRemoveFragmentToStack(withBackPressed: Boolean)
}