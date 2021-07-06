package br.com.pix.conta

import br.com.pix.cliente.ClienteDTOResponse
import io.micronaut.retry.annotation.Fallback
import java.net.http.HttpResponse
import java.util.*

//@Fallback
//class FallbacksAccounts : ExternalAccountApi {
//    override fun listarContas(): ContaDTOResponse {
//        TODO("Not yet implemented")
//    }
//
//    override fun consultaContasIdCliente(id: UUID): ContaDTOResponse {
//        TODO("Not yet implemented")
//    }
//
//    override fun consultaCliente(id: String): HttpResponse<ClienteDTOResponse> {
//        TODO("Not yet implemented")
//        println("FALLBACK: consultaCliente")
//    }
//}