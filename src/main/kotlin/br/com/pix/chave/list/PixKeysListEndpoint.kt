package br.com.pix.chave.list


import br.com.pix.ListaChavePixServiceGrpc
import br.com.pix.ListarChavesPixRequest
import br.com.pix.ListarChavesPixResponse
import br.com.pix.chave.ChavePix
import br.com.pix.chave.ChavePixRepository
import br.com.pix.chave.ExternalPixApi
import br.com.pix.chave.search.PixKeyDetailsResponse
import br.com.pix.chave.search.retornarConsulta
import br.com.pix.errors.handlers.ErrorHandler
import br.com.pix.titular.Owner
import br.com.pix.titular.TipoTitular
import com.google.protobuf.Timestamp
import io.grpc.stub.StreamObserver
import java.lang.IllegalArgumentException
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@ErrorHandler
@Singleton
class PixKeysListEndpoint (@Inject val chavePixRepository: ChavePixRepository
) :  ListaChavePixServiceGrpc.ListaChavePixServiceImplBase(){
    override fun listar(request: ListarChavesPixRequest, responseObserver: StreamObserver<ListarChavesPixResponse>) {
        if (request.clienteId.isNullOrBlank())
            throw IllegalArgumentException("Cliente ID não pode ser nulo ou vazio")

        val lista : List<ChavePix> = chavePixRepository.findByIdCliente(UUID.fromString(request.clienteId))

        responseObserver.onNext(ListarChavesPixResponse.newBuilder()
            .setIdCliente(request.clienteId)
            .addAllChave(conversaoLista(lista))
            .build())
        responseObserver.onCompleted()

    }
}

//fun converter(lista : List<ChavePix>):ListarChavesPixResponse{
//
//    var listaPix: MutableList<PixKey> = mutableListOf<PixKey>()
//    for (i in lista) {
//        val pixKey: PixKey = PixKey(
//            key = i.valorChave,
//            bankAccount =  i.conta.converterBank(),
//            owner = i.conta.converterOwner(),
//            createdAt = i.criadaEm
//        )
//        listaPix.add(pixKey)
//    }
//    return PixKeysListResponse(listaPix)
//}

