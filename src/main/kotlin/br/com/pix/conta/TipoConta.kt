package br.com.pix.conta

enum class TipoConta {
    CONTA_POUPANCA {
        override fun types(): String{
          return  "SVGS"
        }

    }, // SVGS=Conta Poupança
    CONTA_CORRENTE{
        override fun types(): String{
            return  "CACC"
        }


    }, // CACC=Conta Corrente;
    INVALID_ACCOUNT_TYPE{
        override fun types(): String{
            return  ""
        }


    };

    abstract fun types(): String
}