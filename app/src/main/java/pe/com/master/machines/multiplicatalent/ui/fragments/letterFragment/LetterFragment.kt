package pe.com.master.machines.multiplicatalent.ui.fragments.letterFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pe.com.master.machines.multiplicatalent.data.repository.DataSourceRemote
import pe.com.master.machines.multiplicatalent.data.resultData.ResultData
import pe.com.master.machines.multiplicatalent.data.viewModel.LifeViewModel
import pe.com.master.machines.multiplicatalent.data.viewModel.ViewModelFactory
import pe.com.master.machines.multiplicatalent.databinding.LetterFragmentBinding
import pe.com.master.machines.multiplicatalent.helpers.Constants
import pe.com.master.machines.multiplicatalent.ui.activities.mainActivity.MainActivity
import pe.com.master.machines.multiplicatalent.ui.fragments.base.BaseFragment
import javax.inject.Inject

class LetterFragment : BaseFragment() {

    private lateinit var binding: LetterFragmentBinding

    @Inject
    lateinit var dataSourceRemote: DataSourceRemote

    private val viewModel: LifeViewModel by viewModels {
        ViewModelFactory(dataSourceRemote)
    }

    companion object {

        val TAG = LetterFragment::class.java.simpleName

        fun newInstance(autor: String, title: String): LetterFragment {
            Log.d(TAG, "newInstance autor: ${autor}, title: ${title}")
            val fragment = LetterFragment()
            var args = fragment.arguments
            if (args == null)
                args = Bundle()
            args.putString(Constants.Extras.EXTRA_AUTOR, autor)
            args.putString(Constants.Extras.EXTRA_TITLE, title)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getAppComponent().inject(this)
        setHasOptionsMenu(false)
        (activity as MainActivity).viewMenuToolbar(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LetterFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getString(Constants.Extras.EXTRA_AUTOR).let { autor ->
            if (autor != null)
                arguments?.getString(Constants.Extras.EXTRA_TITLE).let { song ->
                    if (song != null)
                        viewModel.searchSong(autor, song)
                }
        }
    }

    override fun onResume() {
        super.onResume()
        observer()
    }

    private fun observer() {
        lifecycleScope.launch {
            viewModel.searchSong.collect {
                when (it) {
                    is ResultData.Loading -> {
                        Log.d(TAG, "observer searchSong esta cargando: ${it}")
                    }
                    is ResultData.Error -> {
                        Log.d(TAG, "observer searchSong error: ${it.throwable.message}")
                        binding.txtLetter.post {
                            binding.txtLetter.text = "No lyrics found"
                        }
                    }
                    is ResultData.Empty -> {
                        Log.d(TAG, "observer searchSong esta vacio: ")
                    }
                    is ResultData.Success -> {
                        Log.d(TAG, "observer searchSong creado con exito: ${it}")
                        binding.txtLetter.post {
                            binding.txtLetter.text = it.data.lyrics
                        }
                    }
                }
            }
        }
    }
}