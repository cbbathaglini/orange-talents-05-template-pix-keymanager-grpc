package br.com.pix.chave.create


import br.com.pix.chave.ChavePix
import br.com.pix.chave.converter
import br.com.pix.conta.Conta
import br.com.pix.key.TipoChave
import br.com.pix.conta.TipoConta
import br.com.pix.validation.PixKeyValidator
import br.com.pix.validation.TypeAccountValidator
import br.com.pix.validation.UUIDValidator
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size



@Introspected
@PixKeyValidator
@TypeAccountValidator
data class ChavePixValidations(

    @field:NotBlank
    @field:UUIDValidator
    val idCliente: String?,

    @field:NotNull
    val tipoConta: TipoConta?,

    @field:NotNull
    val tipoChave: TipoChave?,

    @field:NotBlank @field:Size(max = 77)
    val valorChave: String?


) {


    fun converter(conta:Conta) : ChavePix{
        return ChavePix(
            idCliente = UUID.fromString(this.idCliente),
            tipoChave =  TipoChave.valueOf(this.tipoChave.toString()),
            tipoConta = TipoConta.valueOf(this.tipoConta.toString()),
            valorChave = when(this.tipoChave){
                TipoChave.RANDOM -> UUID.randomUUID().toString()
                else -> this.valorChave!!
            },
            conta = conta
        )
    }
}