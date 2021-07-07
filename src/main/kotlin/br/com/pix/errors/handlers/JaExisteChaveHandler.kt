package br.com.pix.errors.handlers

import io.grpc.Status
import javax.inject.Singleton
import br.com.pix.errors.handlers.ExceptionHandler.StatusWithDetails
import br.com.pix.errors.exceptions.JaExisteChaveException
import br.com.pix.errors.handlers.ExceptionHandler

@Singleton
class JaExisteChaveHandler : ExceptionHandler<JaExisteChaveException> {
    override fun handle(e: JaExisteChaveException):StatusWithDetails {
        return StatusWithDetails(
            Status.ALREADY_EXISTS
                .withDescription(e.message)
                .withCause(e))
    }

    override fun supports(e: Exception): Boolean {
        return e is JaExisteChaveException
    }
}

