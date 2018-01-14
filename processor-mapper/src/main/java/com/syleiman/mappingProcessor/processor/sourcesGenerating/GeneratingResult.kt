package com.syleiman.mappingProcessor.processor.sourcesGenerating

/** */
class GeneratingResult {
    lateinit var fileName : String
    lateinit var packageName : String

    var linesOfCode = mutableListOf<String>()
}