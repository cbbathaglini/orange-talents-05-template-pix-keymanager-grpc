package br.com.pix.cliente

import br.com.pix.instituicao.InstituicaoDTOResponse
import java.util.*

data class ClienteDTOResponse(
    val id : UUID,
    val nome : String,
    val cpf : String,
    val instituicao : InstituicaoDTOResponse
) {
}