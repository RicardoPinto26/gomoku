package pt.isel.leic.daw.gomokuRoyale.http.controllers.home

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pt.isel.leic.daw.gomokuRoyale.http.Actions
import pt.isel.leic.daw.gomokuRoyale.http.Links
import pt.isel.leic.daw.gomokuRoyale.http.Rels
import pt.isel.leic.daw.gomokuRoyale.http.Uris
import pt.isel.leic.daw.gomokuRoyale.http.controllers.home.models.AuthorModel
import pt.isel.leic.daw.gomokuRoyale.http.controllers.home.models.HomeOutputModel
import pt.isel.leic.daw.gomokuRoyale.http.media.siren.SirenEntity

@RestController
class HomeController {

    @GetMapping(Uris.HOME)
    fun getHome(): SirenEntity<HomeOutputModel> {
        val authorsModels = listOf(
            AuthorModel(
                name = "Francisco Medeiros",
                email = "a46631@alunos.isel.pt",
                github = "Francisco-Medeiros00"
            ),
            AuthorModel(
                name = "Luís Macário",
                email = "a47671@alunos.isel.pt",
                github = "Luis-Macario"
            ),
            AuthorModel(
                name = "Ricardo Pinto",
                email = "a47673@alunos.isel.pt",
                github = "RicardoPinto26"
            )
        )

        val homeOutputModel = HomeOutputModel(
            title = "Gomoku Royale",
            version = "1.5.0",
            description = "Gomoku Royale is a game where you can play Gomoku against other players in real time.",
            authors = authorsModels
        )

        return SirenEntity(
            `class` = listOf(Rels.HOME),
            properties = homeOutputModel,
            links = listOf(
                Links.self(Uris.home())
            ),
            actions = listOf(
                Actions.listUsers,
                Actions.register,
                Actions.login
            )
        )
    }
}
