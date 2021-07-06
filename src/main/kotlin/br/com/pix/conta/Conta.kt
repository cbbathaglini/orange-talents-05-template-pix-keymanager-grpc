package br.com.pix.conta
import br.com.pix.instituicao.Instituicao
import br.com.pix.titular.Titular
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Embeddable
data class Conta (
    @field:NotNull
    val tipo : TipoConta,

    @field:NotBlank
    val agencia : String,

    @field:NotBlank
    val numero : String,

    @field:NotBlank
    val nomeTitular : String,

    @field:NotBlank
    val cpfTitular : String,

    @field:NotBlank
    val nomeInstituicao : String,

    @field:NotBlank
    val ispbInstituicao: String
){

}
