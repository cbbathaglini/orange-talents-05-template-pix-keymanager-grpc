package br.com.pix.chave.search

import br.com.pix.*
import br.com.pix.chave.ChavePix
import br.com.pix.chave.ChavePixRepository
import br.com.pix.chave.ExternalPixApi
import br.com.pix.chave.create.CreatePixKeyRequest
import br.com.pix.chave.create.CreatePixKeyResponse
import br.com.pix.cliente.ClienteDTOResponse
import br.com.pix.conta.BankAccount
import br.com.pix.conta.ContaDTOResponse
import br.com.pix.conta.ExternalAccountApi
import br.com.pix.errors.exceptions.ChaveInexistenteException
import br.com.pix.instituicao.InstituicaoDTOResponse
import br.com.pix.titular.Owner
import br.com.pix.titular.TipoTitular
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
import org.mockito.Mockito
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import org.junit.jupiter.api.assertThrows
import javax.inject.Singleton

@MicronautTest(
    rollback = false, //default = true
    transactionMode = TransactionMode.SINGLE_TRANSACTION, //default = TransactionMode.SEPARATE_TRANSACTIONS
    transactional = false //default = true
)
internal class PixKeyDetailsEndpointTest(val grpcClient : ConsultaChavePixServiceGrpc.ConsultaChavePixServiceBlockingStub){
    @Inject
    lateinit var chavePixRepository: ChavePixRepository

    @BeforeEach
    internal fun setup() {
        chavePixRepository.deleteAll()
    }

    @AfterEach
    internal fun tearDown() {
        chavePixRepository.deleteAll()
    }


    @Factory
    class GrpcClientFactory {
        @Singleton
        fun chavePixFactory(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): ConsultaChavePixServiceGrpc.ConsultaChavePixServiceBlockingStub {
            return ConsultaChavePixServiceGrpc.newBlockingStub(channel)
        }
    }

    @Test
    fun `aquele que consulta corretamente pela abordagem 2`(){
        val chavePix = cadastroCassio()

        val response =
            grpcClient.consultar(
                ConsultarChavePixRequest.newBuilder()
                    .setChave("31643468081")
                    .setIdcliente("de95a228-1f27-4ad2-907e-e5a2d816e9bc")
                    .setPixId(chavePix.id.toString())
                    .build()
            )


        //validação
        with(response) {
            assertEquals(response.chave,"31643468081")
            assertEquals(response.idcliente,"de95a228-1f27-4ad2-907e-e5a2d816e9bc")
            assertEquals(response.agencia,"0001")
            assertEquals(response.numero,"084329")
            assertEquals(response.nomeTitular,"Cassio Almeida")
        }

    }

    @Test
    fun `aquele que consulta corretamente pela abordagem 1`(){
        val chavePix = cadastroCassio()

        val response =
            grpcClient.consultar(
                ConsultarChavePixRequest.newBuilder()
                    .setIdcliente("de95a228-1f27-4ad2-907e-e5a2d816e9bc")
                    .setPixId(chavePix.id.toString())
                    .build()
            )


        //validação
        with(response) {
            assertEquals(response.chave,"31643468081")
            assertEquals(response.idcliente,"de95a228-1f27-4ad2-907e-e5a2d816e9bc")
            assertEquals(response.agencia,"0001")
            assertEquals(response.numero,"084329")
            assertEquals(response.nomeTitular,"Cassio Almeida")
        }

    }


    @Test
    fun `aquele que nao consulta corretamente pela abordagem 1`(){
        val chavePix = cadastroCassio()

        val response = assertThrows<StatusRuntimeException> {
            grpcClient.consultar(
                ConsultarChavePixRequest.newBuilder()
                    .setIdcliente("de95a228-1f27-4ad2-907e-e5a2d816e9b7")
                    .setPixId(chavePix.id.toString())
                    .build()
            )
        }

        //validação
        with(response) {
            assertEquals(Status.NOT_FOUND.code, status.code)
            assertEquals("A chave não existe", status.description)
        }

    }

    @Test
    fun `aquele que nao consulta corretamente pela abordagem 1 teste`(){
        val chavePix = cadastroCassio()

        val response = assertThrows<StatusRuntimeException> {
            grpcClient.consultar(
                ConsultarChavePixRequest.newBuilder()
                    .setIdcliente("de95a228-1f27-4ad2-907e-e5a2d816e9bc")
                    .build()
            )
        }

        //validação
        with(response) {
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertEquals("Erro getAbordagem()", status.description)
        }

    }


    private fun cadastroCassio():ChavePix {

        val chavePix :ChavePix = ChavePix(
            idCliente = UUID.fromString("de95a228-1f27-4ad2-907e-e5a2d816e9bc"),
            tipoChave = br.com.pix.key.TipoChave.CPF,
            tipoConta = br.com.pix.conta.TipoConta.CONTA_CORRENTE,
            valorChave = "31643468081",
            conta = ContaCassio().converter()
        )
        chavePixRepository.save(chavePix)
        return chavePix

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