package com.syleiman.mappingProcessor.processor.sourceDataHarvesting.sourceClassesStruct

import javax.lang.model.element.Element
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements

/** */
class ClassInfo {
    companion object
    {
        /** Create from Element */
        fun fromElement(element: Element, elementUtils: Elements) : ClassInfo
        {
            return ClassInfo().apply {
                fullName =  elementUtils.getPackageOf(element).qualifiedName.toString()+"."+element.simpleName.toString()
            }
        }

        /** Create from TypeMirror */
        fun fromType(type: TypeMirror) : ClassInfo
        {
            return ClassInfo().apply {
                fullName = type.toString()
            }
        }
    }

    /** Simple name + package */
    lateinit var fullName: String
}