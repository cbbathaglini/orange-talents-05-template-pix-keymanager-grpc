package br.com.pix.chave.create

import br.com.pix.conta.BankAccount
import br.com.pix.conta.ContaDTOResponse
import br.com.pix.key.TipoChave
import br.com.pix.titular.Owner
import br.com.pix.titular.TitularPixDTOResponse
import java.time.LocalDateTime

data class CreatePixKeyResponse(
    val keyType: TipoChave, //<keyType>CPF</keyType>
    val key : String, //<key>string</key>
    val bankAccount : BankAccount, /*
                                                <bankAccount>
                                                    <participant>string</participant>
                                                    <branch>string</branch>
                                                    <accountNumber>string</accountNumber>
                                                    <accountType>CACC</accountType>
                                                </bankAccount>*/
    val owner : Owner, /*
                                        <owner>
                                            <type>NATURAL_PERSON</type>
                                            <name>string</name>
                                            <taxIdNumber>string</taxIdNumber>
                                        </owner>*/
    val createdAt : LocalDateTime //<createdAt>2021-07-05T14:29:35.479Z</createdAt>
) {
}