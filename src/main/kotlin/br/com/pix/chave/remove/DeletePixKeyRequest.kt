package br.com.pix.chave.remove

import java.time.LocalDateTime

class DeletePixKeyRequest(
    val key : String,
    val participant: String
) {
}