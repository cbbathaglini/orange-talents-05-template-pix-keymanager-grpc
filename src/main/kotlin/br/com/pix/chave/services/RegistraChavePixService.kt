package br.com.pix.chave.services

import br.com.pix.TipoChave
import br.com.pix.chave.ChavePix
import br.com.pix.chave.ChavePixRepository
import br.com.pix.chave.ExternalPixApi
import br.com.pix.chave.converter
import br.com.pix.chave.create.ChavePixValidations
import br.com.pix.chave.create.CreatePixKeyRequest
import br.com.pix.chave.create.CreatePixKeyResponse
import br.com.pix.cliente.ClienteDTOResponse
import br.com.pix.conta.ContaDTOResponse
import br.com.pix.conta.ExternalAccountApi
import br.com.pix.errors.exceptions.ClienteInexistenteException
import br.com.pix.errors.exceptions.JaExisteChaveException
import br.com.pix.errors.exceptions.NaoExisteTipoContaClienteException
import br.com.pix.errors.exceptions.ProibidaOperacaoException
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid


@Validated
@Singleton
class RegistraChavePixService(@Inject val itauService : ExternalAccountApi,
                              @Inject val bcbService : ExternalPixApi,
                              @Inject val chavePixRepository: ChavePixRepository) {
    @Transactional
    fun registrar(@Valid chavePixValidations: ChavePixValidations): ChavePix {

        //validar clientId
        //verifica se o cliente existe
        val clienteDTOResponse : ClienteDTOResponse? = itauService.consultaCliente(chavePixValidations.idCliente!!)
        if(clienteDTOResponse == null){
            throw ClienteInexistenteException("O cliente não existe")
        }

        //conta do cliente
        //verifica que EXISTINDO O CLIENTE, se o tipo de conta informado para este cliente existe
        val contaDTOResponse : ContaDTOResponse? = itauService.consultaContasIdCliente(chavePixValidations.idCliente,chavePixValidations.tipoConta.toString())
        if(contaDTOResponse == null){
            throw NaoExisteTipoContaClienteException("Não existe este tipo de conta associado a este cliente")
        }


        val chavePix : ChavePix = chavePixValidations.converter(contaDTOResponse.converter())
        val createPixKeyRequest : CreatePixKeyRequest  = chavePix.converter(contaDTOResponse,chavePixValidations.tipoConta!!)

        try{
            val createPixKeyResponse : CreatePixKeyResponse = bcbService.criarChavePix(createPixKeyRequest)

            //atualizacoes do pix
            chavePix.atualiza(createPixKeyResponse)

            //verifica se já existe a chave
            if (!chavePix.tipoChave.equals(TipoChave.RANDOM)) { // apenas chaves não aleatórias são verificadas a duplicidade
                if (chavePixRepository.findByValorChave(chavePix.valorChave).isPresent) {
                    throw JaExisteChaveException("A chave PIX cujo valor é '${chavePix.valorChave}', já existe")
                }
            }

            chavePixRepository.save(chavePix)
            return chavePix

        }catch (e : HttpClientResponseException){
            if(e.status == HttpStatus.UNPROCESSABLE_ENTITY){ //status code 422
                throw JaExisteChaveException("Chave pix já registrada")
            }
        }

        throw JaExisteChaveException("A chave PIX já existe no bcb")
    }
}