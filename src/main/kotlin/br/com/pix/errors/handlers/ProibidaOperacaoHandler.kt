package br.com.pix.errors.handlers

import br.com.pix.errors.exceptions.JaExisteChaveException
import br.com.pix.errors.exceptions.ProibidaOperacaoException
import io.grpc.Status
import javax.inject.Singleton


@Singleton
class ProibidaOperacaoHandler : ExceptionHandler<ProibidaOperacaoException> {
    override fun handle(e: ProibidaOperacaoException): ExceptionHandler.StatusWithDetails {
        return ExceptionHandler.StatusWithDetails(
            Status.PERMISSION_DENIED
                .withDescription(e.message)
                .withCause(e)
        )
    }

    override fun supports(e: Exception): Boolean {
        return e is ProibidaOperacaoException
    }
}