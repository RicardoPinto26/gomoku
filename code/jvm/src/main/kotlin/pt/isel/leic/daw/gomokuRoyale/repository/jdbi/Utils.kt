package pt.isel.leic.daw.gomokuRoyale.repository.jdbi

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.postgres.PostgresPlugin
import pt.isel.leic.daw.gomokuRoyale.repository.jdbi.mappers.GameDTOMapper
import pt.isel.leic.daw.gomokuRoyale.repository.jdbi.mappers.GameMapper
import pt.isel.leic.daw.gomokuRoyale.repository.jdbi.mappers.InstantMapper
import pt.isel.leic.daw.gomokuRoyale.repository.jdbi.mappers.LobbyMapper
import pt.isel.leic.daw.gomokuRoyale.repository.jdbi.mappers.TokenValidationInfoMapper

fun Jdbi.configureWithAppRequirements(): Jdbi {
    installPlugin(KotlinPlugin())
    installPlugin(PostgresPlugin())
    registerColumnMapper(TokenValidationInfoMapper())
    registerColumnMapper(InstantMapper())
    registerRowMapper(LobbyMapper())
    registerRowMapper(GameMapper())
    registerRowMapper(GameDTOMapper())
    return this
}
