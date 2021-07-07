package br.com.pix.chave.create

import br.com.pix.*
import br.com.pix.chave.ChavePix
import br.com.pix.chave.converter
import br.com.pix.chave.services.RegistraChavePixService
import br.com.pix.errors.handlers.ErrorHandler
import io.grpc.stub.StreamObserver
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@ErrorHandler
@Singleton
class CreatePixKeyEndpoint(@Inject private val registraService: RegistraChavePixService) :  ChavePIXServiceGrpc.ChavePIXServiceImplBase(){


    override fun salvarChave(request: RegistraChavePixRequest, responseObserver: StreamObserver<RegistraChavePixResponse>?) {


        val chavePixValidations : ChavePixValidations = request.converter()
        val chavePix : ChavePix = registraService.registrar(chavePixValidations)

        responseObserver!!.onNext(RegistraChavePixResponse
            .newBuilder()
            .setPixId(chavePix.id.toString())
            .setChave(chavePix.valorChave)
            .build()
        )
        responseObserver.onCompleted()

    }
}


