package br.com.pix.conta
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
