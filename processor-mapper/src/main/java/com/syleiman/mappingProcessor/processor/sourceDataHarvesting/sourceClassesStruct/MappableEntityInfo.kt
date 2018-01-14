package com.syleiman.mappingProcessor.processor.sourceDataHarvesting.sourceClassesStruct

/** Info about entity with MappableEntity annotation */
class MappableEntityInfo {
    /** Source */
    lateinit var mappableEntity : ClassInfo

    /** Destination */
    lateinit var mappedTo: ClassInfo

    /** List of mappable properties */
    val properties : MutableList<MappablePropertyInfo> = mutableListOf()
}