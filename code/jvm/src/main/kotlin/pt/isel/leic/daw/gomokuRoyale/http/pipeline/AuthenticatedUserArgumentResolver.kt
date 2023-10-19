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

        fun addUserTo(user: AuthenticatedUser, request: HttpServletRequest) {
            logger.info("Adding authentication to user ${user.user.username}")
            return request.setAttribute(KEY, user)
        }

        fun getUserFrom(request: HttpServletRequest): AuthenticatedUser? {
            return request.getAttribute(KEY)?.let {
                logger.info("Getting user")
                it as? AuthenticatedUser
            }
        }
    }
}
