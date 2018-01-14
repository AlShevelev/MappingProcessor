package com.syleiman.mappingProcessor.annotations

import kotlin.reflect.KClass

/** Describes custom parserForward for mapping
 * @param parserForward - parser from source type to target
 * @param parserBackward - parser from target type to source (used only if @MappableEntity::isTwoWay==true)*/
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class MappedBy(val parserForward: KClass<*> = Unit::class, val parserBackward: KClass<*> = Unit::class)