package br.com.pix.errors.handlers

import br.com.pix.errors.exceptions.ChaveInexistenteException
import br.com.pix.errors.exceptions.ClienteInexistenteException
import io.grpc.Status
import javax.inject.Singleton

@Singleton
class ChaveInexistenteHandler : ExceptionHandler<ChaveInexistenteException> {
    override fun handle(e: ChaveInexistenteException): ExceptionHandler.StatusWithDetails {
        return ExceptionHandler.StatusWithDetails(
            Status.NOT_FOUND
                .withDescription(e.message)
                .withCause(e)
        )
    }

    override fun supports(e: Exception): Boolean {
        return e is ChaveInexistenteException
    }
}