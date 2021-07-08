package br.com.pix.chave.remove

import java.time.LocalDateTime

class DeletePixKeyResponse(
    val key : String,
    val participant : String,
    val deletedAt : LocalDateTime
) {
}