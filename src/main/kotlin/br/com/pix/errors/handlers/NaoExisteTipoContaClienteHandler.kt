package br.com.pix.errors.handlers

import br.com.pix.errors.exceptions.NaoExisteTipoContaClienteException
import br.com.pix.errors.handlers.ExceptionHandler
import io.grpc.Status
import javax.inject.Singleton

@Singleton
class NaoExisteTipoContaClienteHandler  : ExceptionHandler<NaoExisteTipoContaClienteException> {
    override fun handle(e: NaoExisteTipoContaClienteException): ExceptionHandler.StatusWithDetails {
        return ExceptionHandler.StatusWithDetails(
            Status.NOT_FOUND
                .withDescription(e.message)
                .withCause(e)
        )
    }

    override fun supports(e: Exception): Boolean {
        return e is NaoExisteTipoContaClienteException
    }
}
