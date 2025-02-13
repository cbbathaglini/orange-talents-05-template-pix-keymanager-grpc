package br.com.pix.chave

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface ChavePixRepository : JpaRepository<ChavePix, UUID> {

    fun findByValorChave(chave:String) : Optional<ChavePix>
    fun findByIdCliente(idCliente: UUID) : List<ChavePix>
    fun findByIdAndIdCliente(idPix:UUID ,idCliente: UUID): Optional<ChavePix>
}