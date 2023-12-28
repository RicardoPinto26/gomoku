package pt.isel.leic.daw.gomokuRoyale.http.pipeline

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import pt.isel.leic.daw.gomokuRoyale.domain.AuthenticatedUser
import pt.isel.leic.daw.gomokuRoyale.http.media.Problem

/**
 * Custom [HandlerInterceptor] for requests that need user authentication.
 *
 * @property authorizationHeaderProcessor a [RequestTokenProcessor] that can parse authorization header and get a user
 * from it
 */
@Component
class AuthenticationInterceptor(
    private val authorizationHeaderProcessor: RequestTokenProcessor
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler is HandlerMethod && handler.methodParameters.any {
            it.parameterType == AuthenticatedUser::class.java
        }
        ) {
            val authHeader = request.getHeader(NAME_AUTHORIZATION_HEADER)
            val tokenCookie = request.cookies?.firstOrNull { logger.info(it.name); it.name == "token" }
            val bearerString = authHeader ?: tokenCookie?.value?.let { "Bearer $it" }

            val user = authorizationHeaderProcessor.processAuthorizationHeaderValue(bearerString)
            return if (user == null) {
                response.status = 401
                response.addHeader(NAME_WWW_AUTHENTICATE_HEADER, RequestTokenProcessor.SCHEME)
                response.contentType = Problem.MEDIA_TYPE
                response.writer.write(
                    Problem.notAuthenticatedString
                )
                false
            } else {
                AuthenticatedUserArgumentResolver.addUserTo(user, request)
                true
            }
        }

        return true
    }

    companion object {

        private val logger = LoggerFactory.getLogger(AuthenticationInterceptor::class.java)
        const val NAME_AUTHORIZATION_HEADER = "Authorization"
        private const val NAME_WWW_AUTHENTICATE_HEADER = "WWW-Authenticate"
    }
}
