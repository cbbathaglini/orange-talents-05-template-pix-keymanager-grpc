package br.com.pix.chave

import br.com.pix.RegistraChavePixRequest
import br.com.pix.chave.create.ChavePixValidations
import br.com.pix.chave.create.CreatePixKeyRequest
import br.com.pix.chave.create.CreatePixKeyResponse
import br.com.pix.cliente.ClienteDTOResponse
import br.com.pix.conta.BankAccount
import br.com.pix.conta.Conta
import br.com.pix.conta.ContaDTOResponse
import br.com.pix.conta.TipoConta
import br.com.pix.key.TipoChave
import br.com.pix.titular.Owner
import br.com.pix.titular.TipoTitular
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

fun ChavePix.converter(contaDTOResponse : ContaDTOResponse,tipoConta: TipoConta) : CreatePixKeyRequest{
    val bankAccount : BankAccount = BankAccount(participant = "60701190",
                                        branch = contaDTOResponse.agencia,
                                        accountNumber = contaDTOResponse.numero,
                                        accountType = tipoConta.converteIngles())
    return CreatePixKeyRequest(
        keyType = TipoChave.valueOf(this.tipoChave.toString()),
        key = this.valorChave,
        bankAccount = bankAccount,
        owner = Owner(type = TipoTitular.NATURAL_PERSON, name = contaDTOResponse.titular.nome, taxIdNumber = contaDTOResponse.titular.cpf)
    )
}