package br.com.pix.chave.list

class PixKeysListResponse (
    val lista : List<PixKey>
){
}

/*
<PixKeysListResponse>
	<pixKeys>
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
	</pixKeys>
</PixKeysListResponse>
 */