package br.com.pix.titular

/*
* <owner>
		<type>NATURAL_PERSON</type>
		<name>string</name>
		<taxIdNumber>string</taxIdNumber>
	</owner>
* */
data class Owner(
    val type : TipoTitular,
    val name : String,
    val taxIdNumber : String
) {
}