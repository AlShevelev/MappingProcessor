package com.syleiman.mappingProcessor.mappers

/** */
interface MapperInterface<in TSource, out TResult> {
    /** */
    fun map(source : TSource) : TResult
}