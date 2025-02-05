package pt.isel.leic.daw.gomokuRoyale.http.pipeline

import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import pt.isel.leic.daw.gomokuRoyale.http.media.Problem

/**
 * Custom [ResponseEntityExceptionHandler] implementation that deals with the Spring application exceptions
 */
@ControllerAdvice
class CustomExceptionHandler : ResponseEntityExceptionHandler() {

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        log.info("Handling MethodArgumentNotValidException: {}", ex.message)
        return Problem.response(Problem.invalidRequestContent)
    }

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        log.info("Handling HttpMessageNotReadableException: {}", ex.message)
        return Problem.response(Problem.invalidRequestContent)
    }

    /**
     * Generic exception handler for all other exceptions.
     *
     * @return a [Problem] response with the status of `unknownInternalException`.
     */
    @ExceptionHandler(
        Exception::class
    )
    fun handleAll(): ResponseEntity<Any> {
        return Problem.response(Problem.unknownInternalException)
    }

    companion object {
        private val log = LoggerFactory.getLogger(CustomExceptionHandler::class.java)
    }
}
