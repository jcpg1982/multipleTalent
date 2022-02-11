package pe.com.master.machines.multiplicatalent.data.repository


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pe.com.master.machines.multiplicatalent.data.model.Lyrics
import pe.com.master.machines.multiplicatalent.data.model.Music
import pe.com.master.machines.multiplicatalent.data.remote.Api
import pe.com.master.machines.multiplicatalent.data.remote.rest.RetrofitSingleton
import retrofit2.http.Query
import javax.inject.Inject

class DataSourceRemote @Inject constructor(private val api: Api) {

    suspend fun getSuggest(dato: String): Flow<Music> {
        return flow {
            val result = api.suggest(dato)
            emit(result)
        }
    }

    suspend fun getSearchSong(autor: String, song: String): Flow<Lyrics> {
        return flow {
            val result = api.searchSong(autor, song)
            emit(result)
        }
    }

    suspend fun getLoadMore(url: String, limit: Int, search: String, index: Int): Flow<Music> {
        return flow {
            val result = RetrofitSingleton(url).request.loadMore(limit, search, index)
            emit(result)
        }
    }
}