package br.com.pix.chave.services

import br.com.pix.RemoverChavePixRequest
import br.com.pix.TipoChave
import br.com.pix.chave.ChavePix
import br.com.pix.chave.ChavePixRepository
import br.com.pix.chave.create.ChavePixValidations
import br.com.pix.cliente.ClienteDTOResponse
import br.com.pix.conta.ContaDTOResponse
import br.com.pix.conta.ExternalAccountApi
import br.com.pix.errors.exceptions.ChaveInexistenteException
import br.com.pix.errors.exceptions.ClienteInexistenteException
import br.com.pix.errors.exceptions.JaExisteChaveException
import br.com.pix.errors.exceptions.NaoExisteTipoContaClienteException
import io.micronaut.validation.Validated
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class RemocaoChavePixService(@Inject val itauService : ExternalAccountApi,
                              @Inject val chavePixRepository: ChavePixRepository
) {
    @Transactional
    fun remover(@Valid request: RemoverChavePixRequest) {

        //val list = chavePixRepository.findAll()

        val chaveEncontrada: Optional<ChavePix> = chavePixRepository.findByIdAndIdCliente(UUID.fromString(request.pixId), UUID.fromString(request.idcliente))
        if (chaveEncontrada.isPresent) {
            chavePixRepository.delete(chaveEncontrada.get())
            return
        }
        throw ChaveInexistenteException("Chave Pix não encontrada ou não pertence ao cliente")
    }

}