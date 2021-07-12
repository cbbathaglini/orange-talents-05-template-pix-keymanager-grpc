package br.com.pix.chave.list

import br.com.pix.*
import br.com.pix.chave.ChavePix
import br.com.pix.chave.ChavePixRepository
import br.com.pix.chave.ExternalPixApi
import br.com.pix.conta.ContaDTOResponse
import br.com.pix.conta.ExternalAccountApi
import br.com.pix.instituicao.InstituicaoDTOResponse
import br.com.pix.key.TipoChave
import br.com.pix.titular.TitularDTOResponse
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.annotation.TransactionMode
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.inject.Inject
import javax.inject.Singleton
import org.junit.jupiter.api.assertThrows
import java.util.*

@MicronautTest(
    rollback = false, //default = true
    transactionMode = TransactionMode.SINGLE_TRANSACTION, //default = TransactionMode.SEPARATE_TRANSACTIONS
    transactional = false //default = true
)
internal class PixKeysListEndpointTest(val grpcClient : ListaChavePixServiceGrpc.ListaChavePixServiceBlockingStub){

    @Inject
    lateinit var chavePixRepository: ChavePixRepository


    @BeforeEach
    internal fun setup(){
        chavePixRepository.deleteAll()
    }

    @AfterEach
    internal fun tearDown(){
        chavePixRepository.deleteAll()
    }

    @Factory
    class GrpcClientFactory {
        @Singleton
        fun chavePixFactory(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel) : ListaChavePixServiceGrpc.ListaChavePixServiceBlockingStub{
            return ListaChavePixServiceGrpc.newBlockingStub(channel)
        }
    }




    @Test
    fun `deve listar corretamente`(){
        cadastroCassio()
        val response =
            grpcClient.listar(
                ListarChavesPixRequest.newBuilder()
                    .setClienteId("de95a228-1f27-4ad2-907e-e5a2d816e9bc")
                    .build()
            )


        //validação
        with(response){
            assertEquals(response.chaveList.size, 3)
        }
    }

    @Test
    fun `nao deve listar pq o id cliente nao foi informado`(){
        val response = assertThrows<StatusRuntimeException> {
            grpcClient.listar(
                ListarChavesPixRequest.newBuilder()
                    .build()
            )
        }

        //validação
        with(response){
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertEquals("O identificador do cliente não pode ser nulo ou vazio", status.description)
        }
    }


    //adicionar 3 chaves
    private fun cadastroCassio() {

        val chavePix : ChavePix = ChavePix(
            idCliente = UUID.fromString("de95a228-1f27-4ad2-907e-e5a2d816e9bc"),
            tipoChave = TipoChave.CPF,
            tipoConta = br.com.pix.conta.TipoConta.CONTA_CORRENTE,
            valorChave = "31643468081",
            conta = ContaCassio().converter()
        )
        chavePixRepository.save(chavePix)


        val chavePixRandom1 : ChavePix = ChavePix(
            idCliente = UUID.fromString("de95a228-1f27-4ad2-907e-e5a2d816e9bc"),
            tipoChave = TipoChave.RANDOM,
            tipoConta = br.com.pix.conta.TipoConta.CONTA_CORRENTE,
            valorChave = UUID.randomUUID().toString(),
            conta = ContaCassio().converter()
        )
        chavePixRepository.save(chavePixRandom1)

        val chavePixRandom2 : ChavePix = ChavePix(
            idCliente = UUID.fromString("de95a228-1f27-4ad2-907e-e5a2d816e9bc"),
            tipoChave = TipoChave.RANDOM,
            tipoConta = br.com.pix.conta.TipoConta.CONTA_CORRENTE,
            valorChave = UUID.randomUUID().toString(),
            conta = ContaCassio().converter()
        )
        chavePixRepository.save(chavePixRandom2)


    }

    private fun ContaCassio() : ContaDTOResponse {
        return ContaDTOResponse(
            tipo = TipoConta.CONTA_CORRENTE.toString(),
            instituicao = InstituicaoDTOResponse(nome = "ITAÚ UNIBANCO S.A.",ispb = "60701190"),
            agencia = "0001",
            numero = "084329",
            titular = TitularDTOResponse(
                id = UUID.fromString("de95a228-1f27-4ad2-907e-e5a2d816e9bc"), nome =  "Cassio Almeida",
                cpf = "31643468081"
            )
        )
    }
}