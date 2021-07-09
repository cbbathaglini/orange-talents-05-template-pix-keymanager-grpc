package br.com.pix.chave.create

import br.com.pix.ChavePIXServiceGrpc
import br.com.pix.RegistraChavePixRequest
import br.com.pix.TipoChave
import br.com.pix.TipoConta
import br.com.pix.chave.ChavePix
import br.com.pix.chave.ChavePixRepository
import br.com.pix.chave.ExternalPixApi
import br.com.pix.cliente.ClienteDTOResponse
import br.com.pix.conta.BankAccount
import br.com.pix.conta.Conta
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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


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

    @Inject
    lateinit var bcbService: ExternalPixApi


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

    @MockBean(ExternalPixApi::class)
    fun bcbServiceMockado(): ExternalPixApi? {
        return Mockito.mock(ExternalPixApi::class.java)
    }



    //aquele em que a rachel descobre
    @Test
    fun `aquele que cadastra uma nova chave pix`(){

        mockCassio("31643468081", br.com.pix.key.TipoChave.CPF,TipoConta.CONTA_CORRENTE)

        val response =
            grpcClient.salvarChave(RegistraChavePixRequest.newBuilder()
                .setChave("31643468081")
                .setClientId("de95a228-1f27-4ad2-907e-e5a2d816e9bc")
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
    fun `aquele em que o tipo de conta eh invalido`(){
//        Mockito.`when`(itauService.consultaCliente("de95a228-1f27-4ad2-907e-e5a2d816e9bc"))
//            .thenReturn(ClienteCassio())
//
//        Mockito.`when`(itauService.consultaContasIdCliente("de95a228-1f27-4ad2-907e-e5a2d816e9bc",TipoConta.INVALID_ACCOUNT_TYPE.toString()))
//            .thenReturn(null)

        mockCassio("31643468081", br.com.pix.key.TipoChave.CPF,TipoConta.INVALID_ACCOUNT_TYPE)

        val response =assertThrows<StatusRuntimeException> {
            grpcClient.salvarChave(
                RegistraChavePixRequest.newBuilder()
                    .setChave("31643468081")
                    .setClientId("de95a228-1f27-4ad2-907e-e5a2d816e9bc")
                    .setTipoDeChave(TipoChave.CPF)
                    .setTipoDeConta(TipoConta.INVALID_ACCOUNT_TYPE)
                    .build()
            )
        }

        //validação
        with(response){
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertEquals("Dados inválidos", status.description)
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
        Mockito.`when`(itauService.consultaCliente("de95a228-1f27-4ad2-907e-e5a2d816e9bc"))
            .thenReturn(ClienteCassio())

        Mockito.`when`(itauService.consultaContasIdCliente("de95a228-1f27-4ad2-907e-e5a2d816e9bc",TipoConta.CONTA_POUPANCA.toString()))
            .thenReturn(null)

        val response = assertThrows<StatusRuntimeException> {
            grpcClient.salvarChave(
                RegistraChavePixRequest.newBuilder()
                    .setChave("31643468081")
                    .setClientId("de95a228-1f27-4ad2-907e-e5a2d816e9bc")//correto é com o último caractere sendo igual a 'b'
                    .setTipoDeChave(TipoChave.CPF)
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
                    .build()
            )
        }
        // validação
        with(thrown) {
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertEquals("Dados inválidos", status.description)
        }

    }

    @Test
    fun `aquele em que ja existe a chave pix pro cliente`(){

        val chaveTemporaria  = chaveTemp(
                            idCliente = UUID.fromString("de95a228-1f27-4ad2-907e-e5a2d816e9bc"),
                            tipoChave =  br.com.pix.key.TipoChave.CPF,
                            tipoConta = br.com.pix.conta.TipoConta.CONTA_CORRENTE,
                            valorChave = "31643468081"
                            )
        chavePixRepository.save(chaveTemporaria)


        mockCassio("31643468081",br.com.pix.key.TipoChave.CPF,TipoConta.CONTA_CORRENTE)

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


    @Test
    fun `aquele em que a chave e randomica`(){

        val chave = UUID.randomUUID().toString()
        mockCassio(chave,br.com.pix.key.TipoChave.RANDOM,TipoConta.CONTA_CORRENTE)

        try {
            val response = grpcClient.salvarChave(
                RegistraChavePixRequest.newBuilder()
                    .setChave(chave)
                    .setClientId("de95a228-1f27-4ad2-907e-e5a2d816e9bc")
                    .setTipoDeChave(TipoChave.RANDOM)
                    .setTipoDeConta(TipoConta.CONTA_CORRENTE)
                    .build()
            )
        }catch (e:Exception){
            println(e)
        }

        // validação
//        with(response) {
//           assertEquals(response.chave,chaveTemporaria.valorChave )
//        }

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


    private fun ClienteCassio() : ClienteDTOResponse{
        return ClienteDTOResponse(
            id = UUID.fromString("de95a228-1f27-4ad2-907e-e5a2d816e9bc"),
            nome = "Cassio Almeida",
            cpf = "31643468081",
            instituicao = InstituicaoDTOResponse(nome = "ITAÚ UNIBANCO S.A.",ispb = "60701190")
        )
    }

    private fun ContaCassio() :  ContaDTOResponse{
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
//        try {
//
////            val testeaux = Mockito.`when`( bcbService.criarChavePix(createPixKeyRequest))
////                .thenReturn(createPixKeyResponse)
//        }catch (e : Exception){
//            println(e)
//        }
    }


}
