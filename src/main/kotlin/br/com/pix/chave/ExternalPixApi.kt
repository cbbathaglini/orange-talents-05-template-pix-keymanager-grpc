package br.com.pix.chave

import br.com.pix.chave.create.CreatePixKeyRequest
import br.com.pix.chave.create.CreatePixKeyResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.client.annotation.Client
import javax.validation.Valid

@Client("http://localhost:8082/api/v1/pix/keys")
interface ExternalPixApi {
    @Post
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    fun criarChavePix(@Body @Valid createPixKeyRequest: CreatePixKeyRequest) : CreatePixKeyResponse


//    @Post
//    @Consumes(MediaType.APPLICATION_XML)
//    fun listarChavesPix() : PixKeysListResponse
//

//    @Post
//    @Consumes(MediaType.APPLICATION_XML)
//    fun buscarChavePix(@Body @Valid createPixKeyRequest: CreatePixKeyRequest) : CreatePixKeyResponse
//
//
//    @Post
//    @Consumes(MediaType.APPLICATION_XML)
//    fun deletarChavePix(@Body @Valid createPixKeyRequest: CreatePixKeyRequest) : CreatePixKeyResponse
}