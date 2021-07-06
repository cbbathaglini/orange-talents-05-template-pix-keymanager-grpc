package br.com.pix.instituicao

data class InstituicaoDTOResponse(
    val nome : String,
    val ispb : String
) {

    fun converter() : Instituicao = Instituicao(nome = this.nome, ispb = this.ispb)

}