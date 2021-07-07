package br.com.pix.chave.create

import br.com.pix.*
import br.com.pix.chave.ChavePix
import br.com.pix.chave.ChavePixRepository
import br.com.pix.conta.Conta
import br.com.pix.conta.ExternalAccountApi
import br.com.pix.errors.exceptions.JaExisteChaveException
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.http.HttpResponse
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
import java.util.*

@MicronautTest(
    rollback = false, //default = true
    transactionMode = TransactionMode.SINGLE_TRANSACTION, //default = TransactionMode.SEPARATE_TRANSACTIONS
    transactional = false //default = true
)
internal class CreatePixKeyEndpointTest(val grpcClient : ChavePIXServiceGrpc.ChavePIXServiceBlockingStub){

    @Inject
    lateinit var chavePixRepository: ChavePixRepository

    @Inject
    lateinit var itauService: ExternalAccountApi

    @BeforeEach
    internal fun setup(){
        chavePixRepository.deleteAll()
    }

    @AfterEach
    internal fun tearDown(){
        chavePixRepository.deleteAll()
    }


    /* motivo????????????????  */
    @Factory
    class GrpcClientFactory {
        @Singleton
        fun chavePixFactory(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel) : ChavePIXServiceGrpc.ChavePIXServiceBlockingStub{
            return ChavePIXServiceGrpc.newBlockingStub(channel)
        }
    }

    @MockBean(ExternalAccountApi::class)
    fun itauServiceMockado(): ExternalAccountApi? {
        return Mockito.mock(ExternalAccountApi::class.java)
    }

    //aquele em que a rachel descobre
    @Test
    fun `aquele que cadastra uma nova chave pix`(){
        val response = grpcClient.salvarChave(RegistraChavePixRequest.newBuilder()
            .setChave("86251791004")
            .setClientId("5260263c-a3c1-4727-ae32-3bdb2538841b")
            .setTipoDeChave(TipoChave.CPF)
            .setTipoDeConta(TipoConta.CONTA_CORRENTE)
            .build())

        //validação
        with(response){
            assertNotNull(pixId)
            assertNotNull(chave)
        }
    }



    @Test
    fun `aquele quando o cliente nao existe e nao deve ser registrado o pix`(){

        val idCliente : String = UUID.randomUUID().toString()
        Mockito.`when`(itauService.consultaCliente(idCliente))
            .thenReturn(null)

        val response = assertThrows<StatusRuntimeException> {
            grpcClient.salvarChave(
                RegistraChavePixRequest.newBuilder()
                    .setChave("86251791004")
                    .setClientId(idCliente)//correto é com o último caractere sendo igual a 'b'
                    .setTipoDeChave(TipoChave.CPF)
                    .setTipoDeConta(TipoConta.CONTA_CORRENTE)
                    .build()
            )
        }

        //validação
        with(response){
            assertEquals(Status.NOT_FOUND.code, status.code)
            assertEquals("O cliente não existe", status.description)
        }
    }

    @Test
    fun `aquele quando o cliente existe mas o tipo de conta informado nao confere`(){
        val response = assertThrows<StatusRuntimeException> {
            grpcClient.salvarChave(
                RegistraChavePixRequest.newBuilder()
                    .setChave("rafael@email.com")
                    .setClientId("c56dfef4-7901-44fb-84e2-a2cefb157890")//correto é com o último caractere sendo igual a 'b'
                    .setTipoDeChave(TipoChave.EMAIL)
                    .setTipoDeConta(TipoConta.CONTA_POUPANCA)
                    .build()
            )
        }

        //validação
        with(response){
            assertEquals(Status.NOT_FOUND.code, status.code) //NaoExisteTipoContaClienteException
            assertEquals("Não existe este tipo de conta associado a este cliente", status.description)
        }
    }

    @Test
    fun `aquele quando os dados do cliente sao invalidos`(){
        val thrown = assertThrows<StatusRuntimeException> {
            grpcClient.salvarChave(
                RegistraChavePixRequest.newBuilder()
                    .setChave("rafael@email.com")
                    .setClientId("c56dfef4-7901-44fb-84e2-a2cefb157890")//correto é com o último caractere sendo igual a 'b'
                    .setTipoDeChave(TipoChave.CPF)
                    .setTipoDeConta(TipoConta.CONTA_CORRENTE)
                    .build()
            )
        }
        // validação
        with(thrown) {
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertEquals("A chave Pix informada é inválida", status.description)
        }

    }

    @Test
    fun `aquele em que ja existe a chave pix pro cliente`(){

        val chaveTemporaria  = chaveTemp(
                            idCliente = UUID.fromString("5260263c-a3c1-4727-ae32-3bdb2538841b"),
                            tipoChave =  br.com.pix.key.TipoChave.CPF,
                            tipoConta = br.com.pix.conta.TipoConta.CONTA_CORRENTE,
                            valorChave = "86251791004"
                            )
        chavePixRepository.save(chaveTemporaria)


        // ação
//        val thrown = assertThrows<StatusRuntimeException> {
//            grpcClient.salvarChave(
//                RegistraChavePixRequest.newBuilder()
//                    .setChave(chaveTemporaria.valorChave)
//                    .setClientId(chaveTemporaria.idCliente.toString())//correto é com o último caractere sendo igual a 'b'
//                    .setTipoDeChave(TipoChave.CPF)
//                    .setTipoDeConta(TipoConta.CONTA_CORRENTE)
//                    .build()
//            )
//        }
        val thrown = assertThrows<StatusRuntimeException> {
            grpcClient.salvarChave(
                RegistraChavePixRequest.newBuilder()
                    .setChave(chaveTemporaria.valorChave)
                    .setClientId(chaveTemporaria.idCliente.toString())//correto é com o último caractere sendo igual a 'b'
                    .setTipoDeChave(TipoChave.CPF)
                    .setTipoDeConta(TipoConta.CONTA_CORRENTE)
                    .build()
            )
        }

        // validação
        with(thrown) {
            assertEquals(Status.ALREADY_EXISTS.code, status.code)
            assertEquals("A chave PIX cujo valor é '${chaveTemporaria.valorChave}', já existe", status.description)
        }

    }


    private fun chaveTemp(
        tipoChave: br.com.pix.key.TipoChave,
        tipoConta: br.com.pix.conta.TipoConta,
        valorChave: String = UUID.randomUUID().toString(),
        idCliente: UUID = UUID.randomUUID(),
    ): ChavePix {
        return ChavePix(
            idCliente = idCliente,
            tipoChave = tipoChave,
            valorChave = valorChave,
            tipoConta = tipoConta,
            conta = Conta(
                nomeInstituicao = "UNIBANCO ITAU",
                ispbInstituicao = "458965",
                nomeTitular = "Rafael Ponte",
                cpfTitular = "63657520325",
                agencia = "1218",
                numero = "291900",
                tipo = tipoConta
            )
        )
    }

}
