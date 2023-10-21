package pt.isel.leic.daw.gomokuRoyale.http.pipeline

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import pt.isel.leic.daw.gomokuRoyale.domain.AuthenticatedUser

/**
 * Custom [HandlerMethodArgumentResolver] that can extract an [AuthenticatedUser] from incoming HTTP requests.
 */
@Component
class AuthenticatedUserArgumentResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter) =
        parameter.parameterType == AuthenticatedUser::class.java

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val request = webRequest.getNativeRequest(HttpServletRequest::class.java)
            ?: throw IllegalStateException("TODO")
        return getUserFrom(request) ?: throw IllegalStateException("TODO")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AuthenticatedUserArgumentResolver::class.java)
        private const val KEY = "AuthenticatedUserArgumentResolver"

        /**
         * Stores a [AuthenticatedUser] object in a [HttpServletRequest] field
         *
         * @param user the AuthenticatedUser object being stored
         * @param request the request received
         */
        fun addUserTo(user: AuthenticatedUser, request: HttpServletRequest) {
            return request.setAttribute(KEY, user)
        }

        /**
         * Gets a [AuthenticatedUser] object from a [HttpServletRequest] field
         *
         * @param request the request received
         *
         * @return AuthenticatedUser object if it exists in the request received, null otherwise
         */
        fun getUserFrom(request: HttpServletRequest): AuthenticatedUser? {
            return request.getAttribute(KEY)?.let {
                it as? AuthenticatedUser
            }
        }
    }
}
