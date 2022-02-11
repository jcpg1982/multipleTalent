package pe.com.master.machines.multiplicatalent

import androidx.fragment.app.FragmentManager

class Utils {

    companion object {

        fun FragmentManager.recursivePopBackStack(): Boolean {
            if (this.fragments != null) {
                for (fragment in this.fragments) {
                    if (fragment != null && fragment.isVisible) {
                        val popped = fragment.childFragmentManager.recursivePopBackStack()
                        if (popped) {
                            return true
                        }
                    }
                }
            }
            if (this.backStackEntryCount > 0) {
                this.popBackStack()
                return true
            }
            return false
        }

    }
}