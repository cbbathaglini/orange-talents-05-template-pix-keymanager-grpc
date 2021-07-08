package br.com.pix.conta

/*
<bankAccount>
		<participant>60701190</participant>
		<branch>0001</branch>
		<accountNumber>123456</accountNumber>
		<accountType>CACC</accountType>
	</bankAccount>
*/
class BankAccount(
    val participant : String, //Identificador SPB do provedor da conta (ex: 60701190 ITAÚ UNIBANCO S.A.). Para ver os demais códigos https://www.bcb.gov.br/pom/spb/estatistica/port/ASTR003.pdf
    val branch : String, //Agência, sem dígito verificador.
    val accountNumber : String, //Número de conta, incluindo verificador. Se verificador for letra, substituir por 0.
    val accountType : String //Tipo de conta (CACC=Conta Corrente; SVGS=Conta Poupança)
) {

}