package br.com.pix.chave.create

import br.com.pix.conta.BankAccount
import br.com.pix.key.TipoChave
import br.com.pix.titular.Owner

class CreatePixKeyRequest(
    val keyType : TipoChave,
    val key : String,
    val bankAccount : BankAccount,
    val owner : Owner
)
{
}

/*
* <CreatePixKeyRequest>
	<keyType>CPF</keyType>
	<key>33059192057</key>
	<bankAccount>
		<participant>60701190</participant>
		<branch>0001</branch>
		<accountNumber>123456</accountNumber>
		<accountType>CACC</accountType>
	</bankAccount>
	<owner>
		<type>NATURAL_PERSON</type>
		<name>Steve Jobs</name>
		<taxIdNumber>33059192057</taxIdNumber>
	</owner>
</CreatePixKeyRequest>*/