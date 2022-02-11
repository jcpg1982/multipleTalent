package pe.com.master.machines.multiplicatalent.data.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import pe.com.master.machines.multiplicatalent.data.model.Lyrics
import pe.com.master.machines.multiplicatalent.data.model.Music
import pe.com.master.machines.multiplicatalent.data.repository.DataSourceRemote
import pe.com.master.machines.multiplicatalent.data.resultData.ResultData

class LifeViewModel(private val dataSourceRemote: DataSourceRemote) : ViewModel() {

    private val TAG = LifeViewModel::class.java.simpleName

    private val _searchMusic: MutableStateFlow<ResultData<Music>> =
        MutableStateFlow(ResultData.First)
    val searchMusic: StateFlow<ResultData<Music>> = _searchMusic

    fun searchMusic(name: String) {
        viewModelScope.launch {
            dataSourceRemote.getSuggest(name)
                .flowOn(Dispatchers.IO)
                .onStart {
                    Log.d(TAG, "searchMusic esta iniciando")
                    _searchMusic.value = ResultData.Loading
                }
                .catch {
                    Log.d(TAG, "searchMusic error: " + it.message)
                    _searchMusic.value = ResultData.Error(it)
                }
                .collect {
                    Log.d(TAG, "searchMusic collect: " + it)
                    if (it.data.size > 0) {
                        _searchMusic.value = ResultData.Success(it)
                    } else {
                        _searchMusic.value = ResultData.Empty
                    }
                }
        }
    }

    private val _searchSong: MutableStateFlow<ResultData<Lyrics>> =
        MutableStateFlow(ResultData.First)
    val searchSong: StateFlow<ResultData<Lyrics>> = _searchSong

    fun searchSong(autor: String, song: String) {
        viewModelScope.launch {
            dataSourceRemote.getSearchSong(autor, song)
                .flowOn(Dispatchers.IO)
                .onStart {
                    Log.d(TAG, "searchMusic esta iniciando")
                    _searchSong.value = ResultData.Loading
                }
                .catch {
                    Log.d(TAG, "searchMusic error: " + it.message)
                    _searchSong.value = ResultData.Error(it)
                }
                .collect {
                    Log.d(TAG, "searchMusic collect: " + it)
                    _searchSong.value = ResultData.Success(it)
                }
        }
    }

    private val _loadMore: MutableStateFlow<ResultData<Music>> =
        MutableStateFlow(ResultData.First)
    val loadMore: StateFlow<ResultData<Music>> = _loadMore

    fun loadMore(url: String, limit: Int, search: String, index: Int) {
        viewModelScope.launch {
            dataSourceRemote.getLoadMore(url, limit, search, index)
                .flowOn(Dispatchers.IO)
                .onStart {
                    Log.d(TAG, "loadMore esta iniciando")
                    _loadMore.value = ResultData.Loading
                }
                .catch {
                    Log.d(TAG, "loadMore error: " + it.message)
                    _loadMore.value = ResultData.Error(it)
                }
                .collect {
                    Log.d(TAG, "loadMore collect: " + it)
                    _loadMore.value = ResultData.Success(it)
                }
        }
    }
}