package br.com.pix.conta

import br.com.pix.cliente.ClienteDTOResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client
import io.micronaut.retry.annotation.Fallback
import java.net.http.HttpResponse
import java.util.*

@Client("http://localhost:9091/api/v1")
interface ExternalAccountApi {

    @Get("/private/contas/todas")
    fun listarContas() : ContaDTOResponse

    @Get("/clientes/{clientId}/contas{?tipo}")
    fun consultaContasIdCliente(@PathVariable("clientId") id : String,@QueryValue("tipo") tipoConta : String ) : ContaDTOResponse

    @Get("/clientes/{clientId}")
    fun consultaCliente(@PathVariable("clientId") id : String) : ClienteDTOResponse
}


//{"tipo":"CONTA_POUPANCA",
// "instituicao":{"nome":"ITAÚ UNIBANCO S.A.","ispb":"60701190"},
// "agencia":"0001","numero":"291900",
// "titular":{"id":"5260263c-a3c1-4727-ae32-3bdb2538841b","nome":"Yuri Matheus","cpf":"86135457004"}
// },
// {"tipo":"CONTA_CORRENTE",
// "instituicao":{"nome":"ITAÚ UNIBANCO S.A.","ispb":"60701190"},
// "agencia":"0001","numero":"123455",
// "titular":{"id":"5260263c-a3c1-4727-ae32-3bdb2538841b","nome":"Yuri Matheus","cpf":"86135457004"}},