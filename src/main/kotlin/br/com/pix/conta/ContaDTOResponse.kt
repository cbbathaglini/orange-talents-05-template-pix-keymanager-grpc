package br.com.pix.conta

import br.com.pix.instituicao.InstituicaoDTOResponse
import br.com.pix.titular.TitularDTOResponse

data class ContaDTOResponse(
    val tipo : String,
    val instituicao: InstituicaoDTOResponse,
    val agencia : String,
    val numero : String,
    val titular: TitularDTOResponse
) {


    fun converter():Conta{
        return Conta(
            tipo = TipoConta.valueOf(this.tipo.toString()),
            agencia = this.agencia,
            numero = this.numero,
            nomeTitular = this.titular.nome,
            cpfTitular = this.titular.cpf,
            nomeInstituicao = this.instituicao.nome,
            ispbInstituicao = this.instituicao.ispb,
        )
    }
}