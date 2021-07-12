package br.com.pix.chave.list

import br.com.pix.ConsultarChavePixResponse
import br.com.pix.ListarChavesPixResponse
import br.com.pix.TipoChave
import br.com.pix.TipoConta
import br.com.pix.chave.ChavePix
import com.google.protobuf.Timestamp
import java.lang.IllegalArgumentException
import java.time.ZoneId
import java.util.*

fun conversaoLista(listaChaves: List<ChavePix>):List<ListarChavesPixResponse.PixKey>{


    return  listaChaves.map {
        ListarChavesPixResponse.PixKey.newBuilder()
            .setIdPix(it.id.toString())
            .setTipoChave(TipoChave.valueOf(it.tipoChave.name))
            .setChave(it.valorChave)
            .setTipoConta(TipoConta.valueOf(it.tipoConta.name))
            .setRegistradoEm(it.criadaEm.let {
                val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                Timestamp.newBuilder()
                    .setSeconds(createdAt.epochSecond)
                    .setNanos(createdAt.nano)
                    .build()
            })
            .build()
    }

}

/*
string idPix = 1;
  string idCliente = 2;
  TipoChave tipoChave = 3;
  string chave = 4;
  TipoConta tipoConta = 5;
  google.protobuf.Timestamp registradoEm = 6;
 */