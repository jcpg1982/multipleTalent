package pe.com.master.machines.multiplicatalent.data.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pe.com.master.machines.multiplicatalent.data.repository.DataSourceRemote

class ViewModelFactory(private val dataSourceRemote: DataSourceRemote) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LifeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LifeViewModel(dataSourceRemote) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}