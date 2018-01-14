package com.syleiman.mappingProcessor.annotations

/** Annotation for property mappable to another property */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class MappableProperty
(val mapTo: String="")