package br.com.pix.chave.search

import br.com.pix.ConsultarChavePixRequest
import br.com.pix.RegistraChavePixRequest
import br.com.pix.chave.ChavePix
import br.com.pix.chave.ChavePixRepository
import br.com.pix.chave.create.ChavePixValidations
import br.com.pix.conta.ContaDTOResponse
import br.com.pix.conta.TipoConta
import br.com.pix.errors.exceptions.ChaveInexistenteException
import br.com.pix.key.TipoChave
import java.util.*
import javax.validation.ConstraintViolationException

fun ConsultarChavePixRequest.getAbordagem() : Int {
    if(this.chave != null){
        return 2
    }
    if(this.idcliente != null && this.pixId != null){
        return 1
    }
    throw Exception("Erro getAbordagem()")
}

fun ConsultarChavePixRequest.abordagemEconsulta(chavePixRepository:ChavePixRepository) : ChavePix{

    if(this.getAbordagem() == 1) {
        val chavePixOp : Optional<ChavePix> = chavePixRepository.findByIdAndIdCliente(UUID.fromString(this.pixId), UUID.fromString(this.idcliente))
        if(!chavePixOp.isPresent){
            throw ChaveInexistenteException("A chave não existe")
        }

        return chavePixOp.get()
    }else{
        val chavePixOp : Optional<ChavePix> = chavePixRepository.findByValorChave(this.chave)
        if(!chavePixOp.isPresent){
            throw ChaveInexistenteException("A chave não existe")
        }
        return chavePixOp.get()
    }


}