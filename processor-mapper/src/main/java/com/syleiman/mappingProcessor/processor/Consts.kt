package com.syleiman.mappingProcessor.processor

import javax.tools.Diagnostic

/** Some constants */
class Consts {
    companion object {
        const val TARGET_FOLDER = "kapt.kotlin.generated"

        const val MAPPABLE_ENTITY = "su.ivcs.mappingProcessor.annotations.MappableEntity"
        const val MAPPABLE_PROPERTY = "com.syleiman.mappingProcessor.annotations.MappableProperty"
        const val MAPPED_BY = "com.syleiman.mappingProcessor.annotations.MappedBy"

        val LOG_LEVEL_DEBUG = Diagnostic.Kind.NOTE
        val LOG_LEVEL_ERROR = Diagnostic.Kind.ERROR
    }
}