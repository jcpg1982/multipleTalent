package pe.com.master.machines.multiplicatalent.data.remote

import pe.com.master.machines.multiplicatalent.data.model.Lyrics
import pe.com.master.machines.multiplicatalent.data.model.Music
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET(URL.SUGGEST)
    suspend fun suggest(@Path("music") dato: String): Music

    @GET(URL.SEARCH_SONG)
    suspend fun searchSong(
        @Path("Artist") artist: String,
        @Path("Song") song: String
    ): Lyrics
}