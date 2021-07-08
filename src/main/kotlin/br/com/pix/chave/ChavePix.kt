package br.com.pix.chave

import br.com.pix.chave.create.CreatePixKeyResponse
import br.com.pix.conta.Conta
import br.com.pix.conta.TipoConta
import br.com.pix.key.TipoChave
import com.sun.istack.NotNull
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
class ChavePix(
    @field:NotNull
    @Column(nullable = false,length = 16) //https://github.com/rafaelpontezup/demo-micronaut-mysql-uuids/blob/master/src/main/kotlin/br/com/zup/edu/Proposal.kt#L13
    val idCliente : UUID,

    @field:NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val tipoChave : TipoChave,

    @field:NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val tipoConta: TipoConta,

    @field:NotBlank
    @field:Column(unique = true, length = 77)
    @field:Size(max = 77)
    var valorChave : String,

    @field:Valid
    @Embedded
    val conta : Conta,

) {

    @Id
    @GeneratedValue
    @Column(length = 16)
    val id : UUID? = null
    //val id : Long? = null

    @CreationTimestamp
    var criadaEm : LocalDateTime = LocalDateTime.now()


    fun atualiza(createPixKeyResponse:CreatePixKeyResponse){
        this.criadaEm = createPixKeyResponse.createdAt
        if(createPixKeyResponse.keyType == TipoChave.RANDOM){
            this.valorChave = createPixKeyResponse.key
        }
    }
}