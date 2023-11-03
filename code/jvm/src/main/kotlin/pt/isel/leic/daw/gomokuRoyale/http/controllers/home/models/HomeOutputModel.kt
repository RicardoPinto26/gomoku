package pt.isel.leic.daw.gomokuRoyale.http.controllers.home.models

data class HomeOutputModel(
    val title: String,
    val version: String,
    val description: String,
    val authors: List<AuthorModel>
)
