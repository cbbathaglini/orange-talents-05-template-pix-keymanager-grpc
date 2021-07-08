package br.com.pix.chave

import br.com.pix.chave.ExternalPixApi
import br.com.pix.chave.create.CreatePixKeyRequest
import br.com.pix.chave.create.CreatePixKeyResponse
import br.com.pix.chave.remove.DeletePixKeyRequest
import br.com.pix.chave.remove.DeletePixKeyResponse
import br.com.pix.cliente.ClienteDTOResponse
import br.com.pix.key.TipoChave
import io.micronaut.http.client.HttpClient
import io.micronaut.retry.annotation.Fallback
import java.net.http.HttpResponse
import java.time.LocalDateTime
import java.util.*

//@Fallback
//class ExternalPixApiFallback : ExternalPixApi {
//    override fun criarChavePix(createPixKeyRequest: CreatePixKeyRequest): CreatePixKeyResponse? {
//
//        return null
//    }
//
//    override fun deletarChavePix(key: String, deletePixKeyRequest: DeletePixKeyRequest): DeletePixKeyResponse? {
//        return null
//    }
//}