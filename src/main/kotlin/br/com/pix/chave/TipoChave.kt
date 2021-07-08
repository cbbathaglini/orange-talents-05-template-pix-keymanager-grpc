package br.com.pix.key
import io.micronaut.validation.validator.constraints.EmailValidator
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator

enum class TipoChave {

    CPF {
        override fun valida(chave: String): Boolean {

            if (!chave.matches("[0-9]+".toRegex())) {
                return false
            }

            return CPFValidator().run {
                initialize(null)
                isValid(chave, null)
            }
        }
    },
    PHONE {
        override fun valida(chave: String): Boolean = chave.matches("^[0-9]{11}\$".toRegex())

    },
    EMAIL {
        override fun valida(chave: String): Boolean = chave.matches("^[a-zA-Z0-9.!#\$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*\$".toRegex())

    },
    RANDOM {
        override fun valida(chave: String) = true
    },
    INVALID_KEY_TYPE{
        override fun valida(chave: String) = false
    };


    abstract fun valida(chave: String): Boolean
}


//
//enum class TipoChave {
//    CPF {
//        override fun validateKey(key: String) = key.matches("^[0-9]{11}\$".toRegex())
//
//    },
//    CNPJ {
//        override fun validateKey(key: String) =  true
//         },
//    PHONE {
//        override fun validateKey(key: String) =  key.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
//
//    },
//
//    EMAIL {
//        override fun validateKey(key: String) = key.matches("^[a-z0-9.]+@[a-z0-9]+\\.[a-z]+\\.?([a-z]+)?\$".toRegex())
//
//    },
//    // o valor da chave não deve ser preenchido pois o mesmo deve ser gerado pelo sistema no formato UUID;
//    RANDOM  {
//        override fun validateKey(key: String) = key.isNullOrBlank()
//
//    },
//    INVALID_KEY_TYPE{
//        override fun validateKey(key: String) = false
//    };
//
//    abstract fun validateKey(key: String): Boolean
//
//
//}