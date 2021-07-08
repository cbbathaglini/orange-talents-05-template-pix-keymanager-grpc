package br.com.pix.chave.services

import br.com.pix.RemoverChavePixRequest
import br.com.pix.TipoChave
import br.com.pix.chave.ChavePix
import br.com.pix.chave.ChavePixRepository
import br.com.pix.chave.ExternalPixApi
import br.com.pix.chave.converter
import br.com.pix.chave.create.ChavePixValidations
import br.com.pix.chave.create.CreatePixKeyRequest
import br.com.pix.chave.create.CreatePixKeyResponse
import br.com.pix.chave.remove.DeletePixKeyRequest
import br.com.pix.chave.remove.DeletePixKeyResponse
import br.com.pix.cliente.ClienteDTOResponse
import br.com.pix.conta.ContaDTOResponse
import br.com.pix.conta.ExternalAccountApi
import br.com.pix.errors.exceptions.*
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.validation.Validated
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Status
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class RemocaoChavePixService(@Inject val itauService : ExternalAccountApi,
                             @Inject val bcbService : ExternalPixApi,
                             @Inject val chavePixRepository: ChavePixRepository
) {
    @Transactional
    fun remover(@Valid request: RemoverChavePixRequest) {

        val list = chavePixRepository.findAll()

        val chaveEncontrada: Optional<ChavePix> = chavePixRepository.findByIdAndIdCliente(UUID.fromString(request.pixId), UUID.fromString(request.idcliente))

        if (chaveEncontrada.isPresent) {
            val deletePixKeyRequest: DeletePixKeyRequest =
                DeletePixKeyRequest(chaveEncontrada.get().valorChave, "60701190")

            try {
                val deletePixKeyResponse: DeletePixKeyResponse =
                    bcbService.deletarChavePix(chaveEncontrada.get().valorChave, deletePixKeyRequest)

                chavePixRepository.delete(chaveEncontrada.get())
                return
            }catch (e : HttpClientResponseException){
               if(e.status == HttpStatus.FORBIDDEN){
                   throw ProibidaOperacaoException("A operação de deletar a chave é proibida")
               }
            }
        }

        throw ChaveInexistenteException("Chave Pix não encontrada ou não pertence ao cliente")
    }

}