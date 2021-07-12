package br.com.pix.conta
import br.com.pix.titular.Owner
import br.com.pix.titular.TipoTitular
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

    fun converterBank() : BankAccount{
        return BankAccount(
            participant = "60701190",
            branch = this.agencia,
            accountNumber = this.numero,
            accountType = this.tipo.toString()
        )
    }

    fun converterOwner() : Owner{
        return Owner(
           type = TipoTitular.NATURAL_PERSON,
            name = this.nomeTitular,
            taxIdNumber = this.cpfTitular
        )
    }

}
