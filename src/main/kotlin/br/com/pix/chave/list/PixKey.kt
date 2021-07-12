package br.com.pix.chave.list

import br.com.pix.conta.BankAccount
import br.com.pix.titular.Owner
import java.time.LocalDateTime

class PixKey(
    val key: String,
    val bankAccount: BankAccount,
    val owner: Owner,
    val createdAt: LocalDateTime
) {
}

/*
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
		<createdAt>2021-07-12T15:09:04.960Z</createdAt>
 */