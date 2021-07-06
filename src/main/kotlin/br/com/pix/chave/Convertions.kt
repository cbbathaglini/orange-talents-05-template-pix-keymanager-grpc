package br.com.pix.chave

import br.com.pix.RegistraChavePixRequest
import br.com.pix.chave.create.ChavePixValidations
import br.com.pix.conta.Conta
import br.com.pix.conta.TipoConta
import br.com.pix.key.TipoChave
import java.util.*

//conversão

fun RegistraChavePixRequest.converter() :ChavePixValidations{
    return ChavePixValidations(
        idCliente = this.clientId,
        tipoChave =  TipoChave.valueOf(this.tipoDeChave.toString()),
        tipoConta = TipoConta.valueOf(this.tipoDeConta.toString()),
        valorChave = this.chave
    )
}