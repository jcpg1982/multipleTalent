package pe.com.master.machines.multiplicatalent.data.model

data class Data(
    var id: Int,
    var readable: Boolean,
    var title: String,
    var title_short: String,
    var title_version: String,
    var link: String,
    var duration: Int,
    var rank: Int,
    var explicit_lyrics: Boolean,
    var explicit_content_lyrics: Int,
    var explicit_content_cover: Int,
    var preview: String,
    var artist: Artist,
    var album: Album,
    var md5_image: String,
    var type: String
)
