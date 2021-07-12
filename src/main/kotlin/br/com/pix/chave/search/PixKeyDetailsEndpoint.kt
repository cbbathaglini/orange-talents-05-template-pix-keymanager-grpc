package br.com.pix.chave.search

import br.com.pix.*
import br.com.pix.chave.ChavePix
import br.com.pix.chave.ChavePixRepository
import br.com.pix.chave.ExternalPixApi
import br.com.pix.chave.converter
import br.com.pix.chave.create.ChavePixValidations
import br.com.pix.chave.services.RegistraChavePixService
import br.com.pix.errors.handlers.ErrorHandler
import com.google.protobuf.Timestamp
import io.grpc.stub.StreamObserver
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton


@ErrorHandler
@Singleton
class PixKeyDetailsEndpoint(@Inject val bcbService : ExternalPixApi,
                            @Inject val chavePixRepository: ChavePixRepository
) :  ConsultaChavePixServiceGrpc.ConsultaChavePixServiceImplBase(){

    override fun consultar(request: ConsultarChavePixRequest, responseObserver: StreamObserver<ConsultarChavePixResponse>) {

        val lista = chavePixRepository.findAll()
        val chavePix : ChavePix  = request.abordagemEconsulta(chavePixRepository)
        if(chavePix != null){
            val pixKeyDetailsResponse : PixKeyDetailsResponse = chavePix.converterConsulta()

            responseObserver.onNext(retornarConsulta(pixKeyDetailsResponse))
            responseObserver.onCompleted()
        }


    }
}