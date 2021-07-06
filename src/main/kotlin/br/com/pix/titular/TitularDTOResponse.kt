package br.com.pix.titular

import br.com.pix.instituicao.Instituicao
import java.util.*

data class TitularDTOResponse(
    val id : UUID,
    val nome : String,
    val cpf : String
) {
    fun converter() : Titular = Titular(id = this.id, nome = this.nome, cpf = this.cpf)
}