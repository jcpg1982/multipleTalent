package pe.com.master.machines.multiplicatalent.data.remote.rest

import pe.com.master.machines.multiplicatalent.data.model.Music
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitRequest {

    @GET("search")
    suspend fun loadMore(
        @Query("limit") limit: Int,
        @Query("q") search: String,
        @Query("index") index: Int
    ): Music
}