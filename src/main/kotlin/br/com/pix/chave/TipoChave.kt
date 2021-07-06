package br.com.pix.key

enum class TipoChave {
    CPF {
        override fun validateKey(key: String) = key.matches("^[0-9]{11}\$".toRegex())

    },
    CNPJ {
        override fun validateKey(key: String) =  true
         },
    PHONE {
        override fun validateKey(key: String) =  key.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())

    },

    EMAIL {
        override fun validateKey(key: String) = key.matches("^[a-z0-9.]+@[a-z0-9]+\\.[a-z]+\\.?([a-z]+)?\$".toRegex())

    },
    // o valor da chave não deve ser preenchido pois o mesmo deve ser gerado pelo sistema no formato UUID;
    RANDOM  {
        override fun validateKey(key: String) = key.isNullOrBlank()

    },
    INVALID_KEY_TYPE{
        override fun validateKey(key: String) = false
    };

    abstract fun validateKey(key: String): Boolean


}