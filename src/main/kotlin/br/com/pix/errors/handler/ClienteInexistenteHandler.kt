package br.com.pix.errors.handler

import io.grpc.Status

class ClienteInexistenteHandler : ExceptionHandler<ClienteInexistenteException>{
    override fun handle(e: ClienteInexistenteException): ExceptionHandler.StatusWithDetails {
        return ExceptionHandler.StatusWithDetails(
            Status.NOT_FOUND
                .withDescription(e.message)
                .withCause(e)
        )
    }

    override fun supports(e: Exception): Boolean {
        return e is ClienteInexistenteException
    }
}