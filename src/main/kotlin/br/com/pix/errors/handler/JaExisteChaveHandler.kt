package br.com.pix.errors.handler

import io.grpc.Status
import javax.inject.Singleton
import br.com.pix.errors.handler.ExceptionHandler
import br.com.pix.errors.handler.ExceptionHandler.StatusWithDetails

@Singleton
class JaExisteChaveHandler : ExceptionHandler<JaExisteChaveException>{
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

