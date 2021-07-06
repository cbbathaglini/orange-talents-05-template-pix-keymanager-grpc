package br.com.pix.validation

import br.com.pix.chave.create.ChavePixValidations
import javax.inject.Singleton

@Singleton
class ValidatePixKey: javax.validation.ConstraintValidator<PixKeyValidator, ChavePixValidations> {

    override fun isValid(pix: ChavePixValidations?, context: javax.validation.ConstraintValidatorContext): Boolean {

        if (pix?.tipoChave == null) {
            return true
        }

        return pix.tipoChave.validateKey(pix.valorChave!!)
    }
}