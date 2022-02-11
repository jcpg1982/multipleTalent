package pe.com.master.machines.multiplicatalent.ui.fragments.homeFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pe.com.master.machines.multiplicatalent.data.repository.DataSourceRemote
import pe.com.master.machines.multiplicatalent.data.resultData.ResultData
import pe.com.master.machines.multiplicatalent.data.viewModel.LifeViewModel
import pe.com.master.machines.multiplicatalent.data.viewModel.ViewModelFactory
import pe.com.master.machines.multiplicatalent.databinding.HomeFragmentBinding
import pe.com.master.machines.multiplicatalent.ui.activities.mainActivity.MainActivity
import pe.com.master.machines.multiplicatalent.ui.adapter.HomeAdapter
import pe.com.master.machines.multiplicatalent.ui.adapter.base.PagListener
import pe.com.master.machines.multiplicatalent.ui.fragments.base.BaseFragment
import pe.com.master.machines.multiplicatalent.ui.fragments.letterFragment.LetterFragment
import java.net.URL
import javax.inject.Inject


class HomeFragment : BaseFragment() {

    private lateinit var binding: HomeFragmentBinding
    private var mAdapter: HomeAdapter? = null
    private var isLoad = false
    private var isLast = false
    private var mLayoutManager: LinearLayoutManager? = null
    private var urlLoadMore: String? = null

    @Inject
    lateinit var dataSourceRemote: DataSourceRemote

    private val viewModel: LifeViewModel by viewModels {
        ViewModelFactory(dataSourceRemote)
    }

    companion object {

        val TAG = HomeFragment::class.java.simpleName

        fun newInstance(): HomeFragment {
            val fragment = HomeFragment()
            var args = fragment.arguments
            if (args == null) {
                args = Bundle()
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getAppComponent().inject(this)
        mLayoutManager = LinearLayoutManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        viewModel.searchMusic("peru")
    }

    override fun onResume() {
        super.onResume()
        observer()
    }


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden)
            (activity as MainActivity).viewMenuToolbar(false)
        else
            (activity as MainActivity).viewMenuToolbar(true)
        Log.d(TAG, "onHiddenChanged hidden: ${hidden}")
    }

    fun searchMusic(name: String) {
        Log.d(TAG, "searchMusic name: ${name}")
        viewModel.searchMusic(name)
    }

    private fun setupRecycler() {
        mAdapter = HomeAdapter(loadImage = { url, imageV ->

            Log.d(TAG, "setupRecycler url: ${url}")

            Glide.with(requireContext())
                .load(url)
                .centerCrop()
                .transform(CircleCrop())
                .into(imageV)

        }, openDetail = { data, imageV ->

            onAddFragmentToStack(
                this,
                LetterFragment.newInstance(data.artist.name, data.title),
                data.title,
                "",
                true,
                true,
                LetterFragment.TAG
            )

        })

        mLayoutManager?.orientation = LinearLayoutManager.VERTICAL
        binding.recyclerHome.setHasFixedSize(true)
        binding.recyclerHome.layoutManager = mLayoutManager
        binding.recyclerHome.adapter = mAdapter
    }

    private fun observer() {
        //searchMusic
        lifecycleScope.launch {
            viewModel.searchMusic.collect {
                when (it) {
                    is ResultData.Loading -> {
                        isLoad = true
                        Log.d(TAG, "observer searchMusic esta cargando: ${it}")
                    }
                    is ResultData.Error -> {
                        isLoad = false
                        Log.d(
                            TAG,
                            "observer searchMusic error: " + it.throwable.message
                        )
                    }
                    is ResultData.Empty -> {
                        isLoad = false
                        Log.d(TAG, "observer searchMusic esta vacio: ")
                    }
                    is ResultData.Success -> {
                        isLoad = false
                        Log.d(TAG, "observer searchMusic creado con exito: ${it}")
                        mAdapter?.let { adapter ->
                            adapter.setData(it.data.data)
                            initScroll(adapter.itemCount - 1)
                            if (!it.data.next.isEmpty()) {
                                urlLoadMore = it.data.next
                                isLast = false
                            }
                        }
                    }
                }
            }

        }

        //loadMore
        lifecycleScope.launch {
            viewModel.loadMore.collect {
                when (it) {
                    is ResultData.Loading -> {
                        Log.d(TAG, "observer loadMore esta cargando: ${it}")
                        isLoad = true
                    }
                    is ResultData.Error -> {
                        isLoad = false
                        Log.d(
                            TAG,
                            "observer loadMore error: " + it.throwable.message
                        )
                    }
                    is ResultData.Empty -> {
                        isLoad = false
                        Log.d(TAG, "observer loadMore esta vacio: ")
                    }
                    is ResultData.Success -> {
                        Log.d(TAG, "observer loadMore creado con exito: ${it}")
                        isLoad = false
                        mAdapter?.let { adapter ->
                            adapter.addData(it.data.data)
                            initScroll(adapter.itemCount - 1)
                            if (!it.data.next.isEmpty()) {
                                urlLoadMore = it.data.next
                                isLast = false
                            }
                        }
                    }
                }
            }

        }
    }

    private fun initScroll(size: Int) {
        mLayoutManager?.let {
            binding.recyclerHome.addOnScrollListener(object : PagListener(it, size) {

                override fun loadMoreItems() {
                    isLoad = true
                    urlLoadMore?.let {
                        val miUrl = URL(it)
                        val url = miUrl.getProtocol() + "://" + miUrl.getHost() + "/"
                        System.out.println("url     = " + url)
                        val split = miUrl.getQuery().split("&")
                        val splitLimit = split[0].split("=")
                        val splitQuery = split[1].split("=")
                        val splitIndex = split[2].split("=")
                        viewModel.loadMore(
                            url,
                            splitLimit[1].toInt(),
                            splitQuery[1],
                            splitIndex[1].toInt()
                        )
                    }
                }

                override val isLastPage: Boolean
                    get() = isLast
                override val isLoading: Boolean
                    get() = isLoad
            })
        }
    }
}