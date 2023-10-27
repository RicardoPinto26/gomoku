package pt.isel.leic.daw.gomokuRoyale.http.media

data class Field(
    val name: String,
    val `class`: List<String>? = null,
    val type: String? = null,
    val value: Any? = null,
    val title: String? = null
)
