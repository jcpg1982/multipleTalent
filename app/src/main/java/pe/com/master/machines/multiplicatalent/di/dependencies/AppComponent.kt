package pe.com.master.machines.multiplicatalent.di.dependencies

import dagger.Component
import pe.com.master.machines.multiplicatalent.di.modulos.AppModule
import pe.com.master.machines.multiplicatalent.di.modulos.NetModule
import pe.com.master.machines.multiplicatalent.ui.fragments.homeFragment.HomeFragment
import pe.com.master.machines.multiplicatalent.ui.fragments.letterFragment.LetterFragment
import javax.inject.Singleton

@Singleton
@Component(
    modules = arrayOf(
        AppModule::class,
        NetModule::class
    )
)
interface AppComponent {

    fun inject(fragment: HomeFragment)

    fun inject(fragment: LetterFragment)
}