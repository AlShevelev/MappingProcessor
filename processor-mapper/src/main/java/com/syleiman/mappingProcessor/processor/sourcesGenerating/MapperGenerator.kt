package com.syleiman.mappingProcessor.processor.sourcesGenerating

import com.syleiman.mappingProcessor.mappers.MapperInterface
import com.syleiman.mappingProcessor.processor.sourceDataHarvesting.sourceClassesStruct.ClassInfo
import com.syleiman.mappingProcessor.processor.sourceDataHarvesting.sourceClassesStruct.MappableEntityInfo

/** Generate mapper */
class MapperGenerator(private val allEntities : List<MappableEntityInfo>) {
    /** */
    fun generate(processedEntity : MappableEntityInfo) : GeneratingResult
    {
        var counter : Int = 1

        val mappers = mutableMapOf<String, String>()            // Key: full name of mappable entity; value: name of mapper instance
        val parsers = mutableMapOf<String, String>()            // Key: full name of parserForward's class; value: name of parserForward instance

        val mapperClassName = getMapperClassName(processedEntity.mappableEntity)
        val packageName = getPackage(processedEntity.mappableEntity)

        val result = GeneratingResult()
        result.fileName = "$mapperClassName.kt"
        result.packageName = packageName

        val mappersAndParsersDeclaration = mutableListOf<String>()          // Child mappers and parsers

        // Package
        result.linesOfCode.add("package $packageName")
        result.linesOfCode.add("")

        // Class
        result.linesOfCode.add("class $mapperClassName : ${MapperInterface::class.qualifiedName}<${processedEntity.mappableEntity.fullName}, ${processedEntity.mappedTo.fullName}>")
        result.linesOfCode.add("{")            // Begin class

            // map method
            result.linesOfCode.add("")
            result.linesOfCode.add("\toverride fun map(source: ${processedEntity.mappableEntity.fullName}): ${processedEntity.mappedTo.fullName}")
            result.linesOfCode.add("\t{")            // Begin method

                // method body
                result.linesOfCode.add("\t\treturn ${processedEntity.mappedTo.fullName}().apply {")        // Begin body

                for(property in processedEntity.properties)
                {
                    val inMappableEntitiesList = isMappedEntitiesList(property.sourceClass)

                    if(inMappableEntitiesList!=null)     // Property type has a mapper
                    {
                        var mapperInstanceName = mappers[property.sourceClass.fullName]
                        if(mapperInstanceName==null)
                        {
                            mapperInstanceName = "mapper${counter++}"
                            mappersAndParsersDeclaration.add("\tprivate val $mapperInstanceName = ${getPackage(property.sourceClass)}.${getMapperClassName(property.sourceClass)}()")
                            mappers.put(property.sourceClass.fullName, mapperInstanceName)
                        }

                        if(property.isNullable)
                            result.linesOfCode.add("\t\t\t${property.destinationName} = source.${property.sourceName}?.let { $mapperInstanceName.map(it) } as ${inMappableEntitiesList.mappedTo.fullName}?")
                        else
                            result.linesOfCode.add("\t\t\t${property.destinationName} = $mapperInstanceName.map(source.${property.sourceName})")
                    }
                    else if(property.parser!=null)          // Property has custom parserFrom
                    {
                        var parserInstanceName = parsers[property.parser!!.fullName]
                        if(parserInstanceName==null)
                        {
                            parserInstanceName = "parserFrom${counter++}"
                            mappersAndParsersDeclaration.add("\tprivate val $parserInstanceName = ${property.parser!!.fullName}()")
                            parsers.put(property.parser!!.fullName, parserInstanceName)
                        }

                        result.linesOfCode.add("\t\t\t${property.destinationName} = $parserInstanceName.parse(source.${property.sourceName})")
                    }
                    else                                    // Simple property
                        result.linesOfCode.add("\t\t\t${property.destinationName} = source.${property.sourceName}")
                }

                result.linesOfCode.add("\t\t}")            // End body

            result.linesOfCode.add("\t}")            // End method

        result.linesOfCode.add("}")            // End class

        result.linesOfCode.addAll(4, mappersAndParsersDeclaration)      // Insert mappers and parsers
        return result
    }

    /** */
    fun getPackage(sourceEntity : ClassInfo) : String = sourceEntity.fullName.substring(0, sourceEntity.fullName.lastIndexOf("."))

    /** */
    fun getMapperClassName(sourceEntity : ClassInfo) : String = "${sourceEntity.fullName.substring(sourceEntity.fullName.lastIndexOf(".")+1)}Mapper"

    /** */
    fun isMappedEntitiesList(sourceEntity : ClassInfo) : MappableEntityInfo? = allEntities.firstOrNull({it.mappableEntity.fullName==sourceEntity.fullName})
}