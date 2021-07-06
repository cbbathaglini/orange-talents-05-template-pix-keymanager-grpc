package br.com.pix.errors.handler

import io.grpc.Status

class NaoExisteTipoContaClienteHandler  : ExceptionHandler<NaoExisteTipoContaClienteException>{
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
