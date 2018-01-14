package com.syleiman.mappingProcessor.processor.sourceDataHarvesting.sourceClassesStruct

import com.syleiman.mappingProcessor.processor.sourceDataHarvesting.sourceClassesStruct.ClassInfo

/** Property with MappableProperty annotation */
class MappablePropertyInfo {
    /** Mappable property's name */
    lateinit var sourceName: String

    /** Mapped to property's name */
    lateinit var destinationName: String

    /** Source property */
    lateinit var sourceClass: ClassInfo

    /** Is source property nullable */
    var isNullable : Boolean = false

    /** Custom parserForward (null - no parserForward) */
    var parser : ClassInfo? = null
}