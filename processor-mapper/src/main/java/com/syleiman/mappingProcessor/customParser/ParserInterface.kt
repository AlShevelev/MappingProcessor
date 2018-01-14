package com.syleiman.mappingProcessor.customParser

/** Interface for all parsers */
interface ParserInterface<in TSource, out TResult> {
    /** */
    fun parse(source : TSource) : TResult
}
