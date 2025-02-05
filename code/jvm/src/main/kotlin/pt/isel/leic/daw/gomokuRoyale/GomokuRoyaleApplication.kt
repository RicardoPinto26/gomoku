package pt.isel.leic.daw.gomokuRoyale

import kotlinx.datetime.Clock
import org.jdbi.v3.core.Jdbi
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import pt.isel.leic.daw.gomokuRoyale.domain.token.Sha256TokenEncoder
import pt.isel.leic.daw.gomokuRoyale.domain.user.UserDomainConfig
import pt.isel.leic.daw.gomokuRoyale.http.pipeline.AuthenticatedUserArgumentResolver
import pt.isel.leic.daw.gomokuRoyale.http.pipeline.AuthenticationInterceptor
import pt.isel.leic.daw.gomokuRoyale.repository.jdbi.configureWithAppRequirements
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

@SpringBootApplication(scanBasePackages = ["pt.isel.leic.daw.gomokuRoyale"])
class GomokuRoyaleApplication {
    @Bean
    fun jdbi() = Jdbi.create(
        PGSimpleDataSource().apply {
            setURL(Environment.getDbUrl())
        }
    ).configureWithAppRequirements()

    @Bean
    fun tokenEncoder() = Sha256TokenEncoder()

    @Bean
    fun clock() = Clock.System

    @Bean
    fun userDomainConfig() = UserDomainConfig(
        tokenSizeInBytes = 256 / 8,
        tokenTtl = 31.days,
        tokenRollingTtl = 1.hours,
        maxTokensPerUser = 3
    )
}

@Configuration
class PipelineConfigurer(
    val authenticationInterceptor: AuthenticationInterceptor,
    val authenticatedUserArgumentResolver: AuthenticatedUserArgumentResolver
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authenticationInterceptor)
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(authenticatedUserArgumentResolver)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowCredentials(true)
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            .allowedOrigins("http://localhost:3000", "http://localhost")
    }
}

fun main(args: Array<String>) {
    runApplication<GomokuRoyaleApplication>(*args)
}
