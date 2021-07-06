package br.com.pix.validation

import javax.inject.Singleton
import javax.validation.Constraint
import br.com.pix.chave.create.ChavePixValidations
import javax.validation.Payload
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.TYPE
import kotlin.reflect.KClass

@MustBeDocumented
@Target(CLASS, TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = [ValidatePixKey::class])
annotation class PixKeyValidator(
    val message: String = "A chave Pix informada é inválida ",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
)



