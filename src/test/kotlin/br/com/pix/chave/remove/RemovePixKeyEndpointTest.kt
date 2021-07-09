package br.com.pix.chave.remove

import br.com.pix.*
import br.com.pix.chave.ChavePixRepository
import br.com.pix.chave.ExternalPixApi
import br.com.pix.chave.create.CreatePixKeyRequest
import br.com.pix.chave.create.CreatePixKeyResponse
import br.com.pix.cliente.ClienteDTOResponse
import br.com.pix.conta.BankAccount
import br.com.pix.conta.ContaDTOResponse
import br.com.pix.conta.ExternalAccountApi
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
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.annotation.TransactionMode
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.inject.Inject
import javax.inject.Singleton
import org.junit.jupiter.api.assertThrows;
import org.mockito.Mockito
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@MicronautTest(
    rollback = false, //default = true
    transactionMode = TransactionMode.SINGLE_TRANSACTION, //default = TransactionMode.SEPARATE_TRANSACTIONS
    transactional = false //default = true
)
internal class RemovePixKeyEndpointTest(
    val grpcClientRemove : RemocaoChavePixServiceGrpc.RemocaoChavePixServiceBlockingStub,
    val grpcClientRegister : ChavePIXServiceGrpc.ChavePIXServiceBlockingStub,) {

    @Inject
    lateinit var chavePixRepository: ChavePixRepository

    @Inject
    lateinit var itauService: ExternalAccountApi

    @Inject
    lateinit var bcbService: ExternalPixApi

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
        fun chavePixFactory(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): RemocaoChavePixServiceGrpc.RemocaoChavePixServiceBlockingStub {
            return RemocaoChavePixServiceGrpc.newBlockingStub(channel)
        }
    }

    @MockBean(ExternalAccountApi::class)
    fun itauServiceMockado(): ExternalAccountApi? {
        return Mockito.mock(ExternalAccountApi::class.java)
    }

    @MockBean(ExternalPixApi::class)
    fun bcbServiceMockado(): ExternalPixApi? {
        return Mockito.mock(ExternalPixApi::class.java)
    }


    @Test
    fun `aquele que nao deve remover pois o cliente nao existe`(){
        val register = registraCliente()

        var list = chavePixRepository.findAll()
        //validação
        with(register){
            assertNotNull(pixId)
            assertNotNull(chave)
            assertEquals(list.size, 1)
        }

        val response = assertThrows<StatusRuntimeException> {
            grpcClientRemove.remover(
                RemoverChavePixRequest.newBuilder()
                    .setIdcliente("5691e96c-86a0-4eac-9f1e-20925c86879f")
                    .setPixId(register.pixId.toString())
                    .build()
            )
        }

            list = chavePixRepository.findAll()

            //validação
            with(response) {
                assertEquals(Status.NOT_FOUND.code, status.code)
                assertEquals("Chave Pix não encontrada ou não pertence ao cliente", status.description)
                assertEquals(list.size, 1)
            }

        }

    @Test
    fun `aquele que remove corretamente`(){
        val register = registraCliente()

        //validação
        with(register){
            assertNotNull(pixId)
            assertNotNull(chave)
        }

        var list = chavePixRepository.findAll()
        //validação
        with(register){
            assertNotNull(pixId)
            assertNotNull(chave)
            assertEquals(list.size, 1)
        }



        val response =  grpcClientRemove.remover(
                RemoverChavePixRequest.newBuilder()
                    .setIdcliente("de95a228-1f27-4ad2-907e-e5a2d816e9bc")
                    .setPixId(register.pixId.toString())
                    .build()
            )

        list = chavePixRepository.findAll()

        //validação
        with(register){
            assertNotNull(pixId)
            assertNotNull(chave)
            assertEquals(list.size, 0)
        }
    }



    private fun ClienteCassio() : ClienteDTOResponse {
        return ClienteDTOResponse(
            id = UUID.fromString("de95a228-1f27-4ad2-907e-e5a2d816e9bc"),
            nome = "Cassio Almeida",
            cpf = "31643468081",
            instituicao = InstituicaoDTOResponse(nome = "ITAÚ UNIBANCO S.A.",ispb = "60701190")
        )
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

    private fun registraCliente(): RegistraChavePixResponse {


        mockCassio("31643468081", br.com.pix.key.TipoChave.CPF,TipoConta.CONTA_CORRENTE)

        return  grpcClientRegister.salvarChave(RegistraChavePixRequest.newBuilder()
            .setChave("31643468081")
            .setClientId("de95a228-1f27-4ad2-907e-e5a2d816e9bc")
            .setTipoDeChave(TipoChave.CPF)
            .setTipoDeConta(TipoConta.CONTA_CORRENTE)
            .build())
    }



    private fun mockCassio(chave : String,  tipoChave : br.com.pix.key.TipoChave, tipoConta: TipoConta) {

        var conta : ContaDTOResponse? = ContaCassio()

        if(tipoConta == TipoConta.INVALID_ACCOUNT_TYPE || tipoChave == br.com.pix.key.TipoChave.INVALID_KEY_TYPE){
            conta =  null
        }

        Mockito.`when`(itauService.consultaCliente("de95a228-1f27-4ad2-907e-e5a2d816e9bc"))
            .thenReturn(ClienteCassio())


        Mockito.`when`(itauService.consultaContasIdCliente("de95a228-1f27-4ad2-907e-e5a2d816e9bc",tipoConta.toString()))
            .thenReturn(conta)


        val contaCassio = ContaCassio()
        val bankAccount : BankAccount = BankAccount(participant = "60701190",
            branch = contaCassio.agencia,
            accountNumber = contaCassio.numero,
            accountType = "CACC"
        )

        val owner : Owner =  Owner(
            type = TipoTitular.NATURAL_PERSON,
            name = contaCassio.titular.nome,
            taxIdNumber =contaCassio.titular.cpf
        )

        val createPixKeyRequest : CreatePixKeyRequest = CreatePixKeyRequest(
            keyType = tipoChave,
            key = chave,
            bankAccount = bankAccount,
            owner =owner
        )


        val str = "2021-07-08 14:35"
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val dateTime = LocalDateTime.parse(str, formatter)

        val createPixKeyResponse : CreatePixKeyResponse = CreatePixKeyResponse(
            keyType = tipoChave,
            key = chave,
            bankAccount = bankAccount,
            owner = owner,
            createdAt = dateTime
        )

        println("CreatePixKeyRequest: ${createPixKeyRequest}")
        Mockito.doReturn(createPixKeyResponse).`when`(bcbService).criarChavePix(createPixKeyRequest)

    }
}