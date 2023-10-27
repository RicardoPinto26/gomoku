package pt.isel.leic.daw.gomokuRoyale.http.media

data class SirenEntity<T>(
    val `class`: List<String>? = null,
    val properties: T? = null,
    val entities: List<SubEntity>? = null,
    val links: List<Link>? = null,
    val actions: List<Action>? = null,
    val title: String? = null
)
