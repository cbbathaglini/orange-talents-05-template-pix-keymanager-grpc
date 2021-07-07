package br.com.pix.validation

import br.com.pix.chave.create.ChavePixValidations
import br.com.pix.conta.TipoConta
import javax.inject.Singleton


@Singleton
class ValidateTypeAccount: javax.validation.ConstraintValidator<TypeAccountValidator, ChavePixValidations> {

    override fun isValid(pix: ChavePixValidations?, context: javax.validation.ConstraintValidatorContext): Boolean {

        if (pix?.tipoConta == null) {
            return false
        }

        val ret : Boolean = pix.tipoConta != TipoConta.INVALID_ACCOUNT_TYPE
        return pix.tipoConta != TipoConta.INVALID_ACCOUNT_TYPE
    }
}