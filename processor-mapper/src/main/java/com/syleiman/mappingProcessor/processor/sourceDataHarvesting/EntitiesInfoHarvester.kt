package com.syleiman.mappingProcessor.processor.sourceDataHarvesting

import com.syleiman.mappingProcessor.annotations.MappableEntity
import com.syleiman.mappingProcessor.annotations.MappableProperty
import com.syleiman.mappingProcessor.annotations.MappedBy
import com.syleiman.mappingProcessor.processor.Consts
import com.syleiman.mappingProcessor.processor.sourceDataHarvesting.sourceClassesStruct.ClassInfo
import com.syleiman.mappingProcessor.processor.sourceDataHarvesting.sourceClassesStruct.MappableEntityInfo
import com.syleiman.mappingProcessor.processor.sourceDataHarvesting.sourceClassesStruct.MappablePropertyInfo
import org.jetbrains.annotations.Nullable
import javax.annotation.processing.Messager
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.util.Elements

/** Harvests info about map entities */
class EntitiesInfoHarvester(private var env: RoundEnvironment, private var logger: Messager, private var elementUtils: Elements) {
    /** */
    fun harvest() : List<MappableEntityInfo> {
        val result = mutableListOf<MappableEntityInfo>()

        // Get all mapped entities
        val annotatedElements = env.getElementsAnnotatedWith(MappableEntity::class.java)
        if (annotatedElements.isEmpty())
            return result

        logger.printMessage(Consts.LOG_LEVEL_DEBUG, "Total mappable entities: "+annotatedElements.size)

        for (element in annotatedElements) {
            // Collect general into
            val mappableEntityInfoForward = MappableEntityInfo().apply {
                mappableEntity = ClassInfo.fromElement(element, elementUtils)
                mappedTo = getMappedToEntity(element)
            }

            logger.printMessage(Consts.LOG_LEVEL_DEBUG, "Forward entity: ${mappableEntityInfoForward.mappableEntity.fullName}; " +
                "Mapped to: ${mappableEntityInfoForward.mappedTo.fullName}")

            // Process properties
            val isJava = isMapFromJava(element)
            val members = elementUtils.getAllMembers(elementUtils.getTypeElement(mappableEntityInfoForward.mappableEntity.fullName))
            for (member in members) {
                if(member.kind!= ElementKind.FIELD)     // Skip not properties
                    continue

                if(!checkIsMappable(member))            // Skip properties without MappableProperty annotation
                    continue

                val propertyInfo = MappablePropertyInfo().apply {
                    sourceName = member.simpleName.toString()
                    sourceClass = ClassInfo.fromType(member.asType())
                    isNullable = checkIsNullable(member, isJava)
                    destinationName = getDestinationPropertyName(member) ?: member.simpleName.toString()
                    parser = getParserForward(member)
                }

                mappableEntityInfoForward.properties.add(propertyInfo)

                logger.printMessage(Consts.LOG_LEVEL_DEBUG, "Property. SourceName: ${propertyInfo.sourceName}; " +
                    "SourceClass: ${propertyInfo.sourceClass.fullName}; IsNullable: ${propertyInfo.isNullable}; " +
                    "DestinationName: ${propertyInfo.destinationName}; Parser: ${propertyInfo.parser?.fullName}")
            }

            result.add(mappableEntityInfoForward)
            logger.printMessage(Consts.LOG_LEVEL_DEBUG, "-------------------------------------------------------------------------")

            // Need two generate backward mapper
            if(isTwoWay(element)){
                // Collect general into
                val mappableEntityBackward = MappableEntityInfo().apply {
                    mappableEntity = mappableEntityInfoForward.mappedTo
                    mappedTo = mappableEntityInfoForward.mappableEntity
                }

                logger.printMessage(Consts.LOG_LEVEL_DEBUG, "Backward entity: ${mappableEntityBackward.mappableEntity.fullName}; " +
                    "Mapped to: ${mappableEntityBackward.mappedTo.fullName}")

                val membersSource = elementUtils.getAllMembers(elementUtils.getTypeElement(mappableEntityInfoForward.mappableEntity.fullName))
                val membersTarget = elementUtils.getAllMembers(elementUtils.getTypeElement(mappableEntityInfoForward.mappedTo.fullName))

                // Process properties (from forward entity to backward!)
                val isJava = isMapToJava(element)
                for (member in membersSource) {
                    logger.printMessage(Consts.LOG_LEVEL_DEBUG, "Source property name: ${member.simpleName}")

                    if(member.kind!= ElementKind.FIELD)     // Skip not properties
                        continue

                    if(!checkIsMappable(member))            // Skip properties without MappableProperty annotation
                        continue

                    val mappedPropertyName = getDestinationPropertyName(member) ?: member.simpleName.toString()

                    logger.printMessage(Consts.LOG_LEVEL_DEBUG, "Property name: $mappedPropertyName")

                    val prop = membersTarget.firstOrNull({it.simpleName.toString()==mappedPropertyName})

                    if(prop==null) {
                        logger.printMessage(Consts.LOG_LEVEL_DEBUG, "not found - skip")
                        continue
                    }

                    val propertyInfo = MappablePropertyInfo().apply {
                        sourceName = mappedPropertyName
                        sourceClass = ClassInfo.fromType(prop!!.asType())
                        isNullable = checkIsNullable(prop!!, isJava)
                        destinationName = member.simpleName.toString()
                        parser = getParserBackward(member)
                    }

                    mappableEntityBackward.properties.add(propertyInfo)

                    logger.printMessage(Consts.LOG_LEVEL_DEBUG, "Property. SourceName: ${propertyInfo.sourceName}; " +
                        "SourceClass: ${propertyInfo.sourceClass.fullName}; IsNullable: ${propertyInfo.isNullable}; " +
                        "DestinationName: ${propertyInfo.destinationName}; Parser: ${propertyInfo.parser?.fullName}")
                }

                result.add(mappableEntityBackward)
                logger.printMessage(Consts.LOG_LEVEL_DEBUG, "-------------------------------------------------------------------------")
            }
        }

        return result
    }

    /** */
    private fun getMappedToEntity(element : Element) : ClassInfo
    {
        try
        {
            // There is always exception here
            // See this: https://community.oracle.com/thread/1184190?start=0
            // and this: https://area-51.blog/2009/02/13/getting-class-values-from-annotations-in-an-annotationprocessor/
            element.getAnnotation(MappableEntity::class.java).mapTo
        } catch (ex : MirroredTypeException) {
            return ClassInfo.fromType(ex.typeMirror)
        }
        throw Exception("Not expected result Excepted MirroredTypeException")
    }

    /** */
    private fun isTwoWay(element : Element) : Boolean
    {
        return element.getAnnotation(MappableEntity::class.java).isTwoWay
    }

    /** */
    private fun isMapFromJava(element : Element) : Boolean
    {
        return element.getAnnotation(MappableEntity::class.java).isMapFromJava
    }

    /** */
    private fun isMapToJava(element : Element) : Boolean
    {
        return element.getAnnotation(MappableEntity::class.java).isMapToJava
    }

    /** */
    private fun checkIsNullable(element : Element, isJava: Boolean) : Boolean {
        if(isJava)
            return true
        return element.getAnnotation(Nullable::class.java)!=null
    }

    /** */
    private fun checkIsMappable(element : Element) : Boolean = element.getAnnotation(MappableProperty::class.java)!=null

    /** */
    private fun getDestinationPropertyName(element : Element) : String?  =
        (element.getAnnotation(MappableProperty::class.java).mapTo).let { if(it=="") null else it }

    /** */
    private fun getParserForward(element : Element) : ClassInfo?
    {
        try
        {
            // see comment in getMappedToEntity method
            val parserAnnotation = element.getAnnotation(MappedBy::class.java) ?: return null
            parserAnnotation.parserForward
        } catch (ex : MirroredTypeException) {
            return ClassInfo.fromType(ex.typeMirror)
        }
        throw Exception("Not expected result Excepted MirroredTypeException")
    }

    /** */
    private fun getParserBackward(element : Element) : ClassInfo?
    {
        try
        {
            // see comment in getMappedToEntity method
            val parserAnnotation = element.getAnnotation(MappedBy::class.java) ?: return null
            parserAnnotation.parserBackward
        } catch (ex : MirroredTypeException) {
            return ClassInfo.fromType(ex.typeMirror)
        }
        throw Exception("Not expected result Excepted MirroredTypeException")
    }
}