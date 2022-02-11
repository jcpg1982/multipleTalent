package pe.com.master.machines.multiplicatalent.ui.fragments.base

import androidx.fragment.app.Fragment
import pe.com.master.machines.multiplicatalent.di.dependencies.AppComponent
import pe.com.master.machines.multiplicatalent.ui.activities.base.BaseActivity

abstract class BaseFragment : Fragment() {

    fun getBaseActivity(): BaseActivity {
        return activity as BaseActivity
    }

    fun getAppComponent(): AppComponent {
        return getBaseActivity().getAppComponent()
    }

    fun onAddFragmentToStack(
        fragmentOrigen: Fragment?,
        fragmentDestino: Fragment,
        title: CharSequence,
        subTitle: CharSequence?,
        addToBackStack: Boolean,
        isAnimated: Boolean,
        tag: String
    ) {
        getBaseActivity().onAddFragmentToStack(
            fragmentOrigen, fragmentDestino, title, subTitle, addToBackStack, isAnimated, tag
        )
    }

    fun onRemoveFragmentToStack(withBackPressed: Boolean) {
        getBaseActivity().onRemoveFragmentToStack(withBackPressed)
    }
}