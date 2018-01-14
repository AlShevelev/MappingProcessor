package com.syleiman.mappingProcessor.processor

import com.syleiman.mappingProcessor.processor.sourceDataHarvesting.EntitiesInfoHarvester
import com.syleiman.mappingProcessor.processor.sourcesGenerating.GeneratingResult
import com.syleiman.mappingProcessor.processor.sourcesGenerating.MapperGenerator
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

@SupportedOptions(Consts.TARGET_FOLDER)
@SupportedAnnotationTypes(Consts.MAPPABLE_ENTITY, Consts.MAPPABLE_PROPERTY, Consts.MAPPED_BY)
class MappingProcessor : AbstractProcessor() {
    private lateinit var typeUtils: Types
    private lateinit var elementUtils: Elements
    private lateinit var filer: Filer
    private lateinit var messager: Messager
    private lateinit var options: Map<String, String>

    private var generated = false

    @Synchronized override fun init(env: ProcessingEnvironment) {
        typeUtils = env.typeUtils
        elementUtils = env.elementUtils
        filer = env.filer
        messager = env.messager
        options = env.options

        messager.printMessage(Consts.LOG_LEVEL_DEBUG, "Initialized in Kotlin")
    }

    /** */
    override fun process(annotations: Set<TypeElement>, env: RoundEnvironment): Boolean {
        messager.printMessage(Consts.LOG_LEVEL_DEBUG, "Processing in Kotlin ...")

        try
        {
            if (!generated) {             // Only one round
                generateFiles(env)
                generated = true
            }
        } catch (ex: Exception) {
            messager.printMessage(Consts.LOG_LEVEL_ERROR, ex.toString())
        }

        return true
    }

    /** */
    private fun generateFiles(env: RoundEnvironment) {
        val baseFolder = getBaseFolder()
        messager.printMessage(Consts.LOG_LEVEL_DEBUG, "Target dir is: $baseFolder")

        // Collect source data
        val sourceData = EntitiesInfoHarvester(env, messager, elementUtils).harvest()

        val generator = MapperGenerator(sourceData)

        sourceData
            .map { generator.generate(it) }
            .forEach { saveToFile(it, baseFolder) }
    }

    /** */
    private fun saveToFile(generatedData : GeneratingResult, baseFolder: String) {
        File(getTargetFolder(baseFolder, generatedData.packageName), generatedData.fileName).apply {
            parentFile.mkdirs()
            writer().buffered().use {
                generatedData.linesOfCode.forEach { line -> it.appendln(line) }
            }
        }
    }

    /** */
    override fun getSupportedSourceVersion() = SourceVersion.RELEASE_7

    /** */
    private fun getBaseFolder() : String
    {
        var baseFolder = options[Consts.TARGET_FOLDER] ?: run {
            throw IllegalArgumentException("Can't find the target directory for generated Kotlin files (options ${Consts.TARGET_FOLDER} is missing")
        }
        baseFolder = baseFolder.replace("kaptKotlin", "kapt")
        return baseFolder
    }

    /** */
    private fun getTargetFolder(baseFolder: String, packageName : String) : String =
        baseFolder+ File.separator+packageName.replace(".", File.separator)
}