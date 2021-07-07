package br.com.pix.chave.remove

import br.com.pix.RemocaoChavePixServiceGrpc
import br.com.pix.RemoverChavePixRequest
import br.com.pix.RemoverChavePixResponse
import br.com.pix.chave.ChavePix
import br.com.pix.chave.create.ChavePixValidations
import br.com.pix.chave.services.RegistraChavePixService
import br.com.pix.chave.services.RemocaoChavePixService
import br.com.pix.errors.handlers.ErrorHandler
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton

@ErrorHandler
@Singleton
class RemovePixKeyEndpoint(@Inject private val remocaoService: RemocaoChavePixService) :  RemocaoChavePixServiceGrpc.RemocaoChavePixServiceImplBase(){

    override fun remover(request: RemoverChavePixRequest, responseObserver: StreamObserver<RemoverChavePixResponse>?) {

        remocaoService.remover(request)
        responseObserver!!.onNext(RemoverChavePixResponse
            .newBuilder()
            .setPixId(request.pixId)
            .setIdcliente(request.idcliente)
            .build()
        )
        responseObserver.onCompleted()

    }
}

