package br.com.pix.chave.search

import br.com.pix.conta.BankAccount
import br.com.pix.key.TipoChave

import br.com.pix.titular.Owner
import java.time.LocalDateTime
import java.util.*

class PixKeyDetailsResponse(
    val pixId : UUID,
    val clientId : UUID,
    val keyType : TipoChave,
    val key : String,
    val bankAccount: BankAccount,
    val owner: Owner,
    val createdAt : LocalDateTime
) {
}

/*
* <PixKeyDetailsResponse>
	<keyType>CPF</keyType>
	<key>string</key>
	<bankAccount>
		<participant>string</participant>
		<branch>string</branch>
		<accountNumber>string</accountNumber>
		<accountType>CACC</accountType>
	</bankAccount>
	<owner>
		<type>NATURAL_PERSON</type>
		<name>string</name>
		<taxIdNumber>string</taxIdNumber>
	</owner>
	<createdAt>2021-07-09T19:12:14.719Z</createdAt>
</PixKeyDetailsResponse>*/