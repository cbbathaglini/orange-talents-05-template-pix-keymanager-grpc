package br.com.pix.titular

import java.util.*

data class TitularDTOResponse(
    val id : UUID,
    val nome : String,
    val cpf : String
) {

}