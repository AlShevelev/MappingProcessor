package com.syleiman.mappingProcessor.annotations

import kotlin.reflect.KClass

/** Annotation for class mappable to another class
 * @param  isTwoWay - need to generate backward parser
 * @param isMapFromJava if @true source file is Java code (otherwise - Kotlin)
 * @param isMapToJava if @true target file is Java code (otherwise - Kotlin) */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class MappableEntity(
    val mapTo : KClass<*>,
    val isTwoWay : Boolean = true,
    val isMapFromJava : Boolean = false,
    val isMapToJava : Boolean = false)
