package br.com.pix.chave.create

import br.com.pix.*
import br.com.pix.chave.ChavePix
import br.com.pix.chave.ChavePixRepository
import br.com.pix.chave.converter
import br.com.pix.chave.services.RegistraChavePixService
import br.com.pix.cliente.ClienteDTOResponse
import br.com.pix.conta.ContaDTOResponse
import br.com.pix.conta.ExternalAccountApi
import br.com.pix.errors.handler.JaExisteChaveException
import io.grpc.Status
import io.grpc.stub.StreamObserver
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


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



//@ErrorHandler // 1
//@Singleton
//class RegistraChaveEndpoint(@Inject private val service: NovaChavePixService,) // 1
//    : KeymanagerRegistraGrpcServiceGrpc.KeymanagerRegistraGrpcServiceImplBase() { // 1
//
//    // 8
//    override fun registra(
//        request: RegistraChavePixRequest, // 1
//        responseObserver: StreamObserver<RegistraChavePixResponse> // 1
//    ) {
//
//        val novaChave = request.toModel() // 2
//        val chaveCriada = service.registra(novaChave) // 1
//
//        responseObserver.onNext(RegistraChavePixResponse.newBuilder() // 1
//            .setClienteId(chaveCriada.clienteId.toString())
//            .setPixId(chaveCriada.id.toString())
//            .build())
//        responseObserver.onCompleted()
//    }
//
//}