package br.com.pix.chave.search

import br.com.pix.ConsultarChavePixResponse
import br.com.pix.TipoChave
import br.com.pix.TipoConta
import br.com.pix.chave.ChavePix
import com.google.protobuf.Timestamp

import java.time.ZoneId

fun retornarConsulta(pixKeyDetailsResponse:PixKeyDetailsResponse): ConsultarChavePixResponse{
    return ConsultarChavePixResponse
        .newBuilder()
        .setAgencia(pixKeyDetailsResponse.bankAccount.branch)
        .setNumero(pixKeyDetailsResponse.bankAccount.accountNumber)
        .setNomeInstituicao(pixKeyDetailsResponse.bankAccount.participant)
        .setChave(pixKeyDetailsResponse.key)
        .setTipoChave(TipoChave.valueOf(pixKeyDetailsResponse.keyType.toString()))
        .setTipoConta(TipoConta.valueOf(pixKeyDetailsResponse.bankAccount.accountType.toString()))
        .setNomeTitular(pixKeyDetailsResponse.owner.name)
        .setCpfTitular(pixKeyDetailsResponse.owner.taxIdNumber)
        .setCriadaEm(pixKeyDetailsResponse.createdAt.let {
            val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
            Timestamp.newBuilder()
                .setSeconds(createdAt.epochSecond)
                .setNanos(createdAt.nano)
                .build()
        })
        .setIdcliente(pixKeyDetailsResponse.clientId.toString())
        .setPixId(pixKeyDetailsResponse.pixId.toString())
        .build()
}