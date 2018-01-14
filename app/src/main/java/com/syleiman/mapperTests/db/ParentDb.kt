package com.syleiman.mapperTests.db

import com.syleiman.mapperTests.customClasses.TestEntity1
import com.syleiman.mapperTests.customClasses.TestEnum
import com.syleiman.mapperTests.customParsers.ChildListParser
import com.syleiman.mapperTests.customParsers.ChildListParserNullable
import com.syleiman.mapperTests.customParsers.TestEntityParser
import com.syleiman.mapperTests.customParsers.TestEntityParserNullable
import com.syleiman.mappingProcessor.annotations.MappableEntity
import com.syleiman.mapperTests.dto.ParentDto
import com.syleiman.mappingProcessor.annotations.MappableProperty
import com.syleiman.mappingProcessor.annotations.MappedBy
import java.util.*

/** */
@MappableEntity(mapTo = ParentDto::class, isTwoWay = false)
class ParentDb
{
    @MappableProperty
    var propShort : Short = 0

    @MappableProperty
    var propShortNullable : Short? = null

    @MappableProperty
    var propInt : Int = 0

    @MappableProperty
    var propIntNullable : Int? = null

    @MappableProperty
    var propLong : Long = 0

    @MappableProperty
    var propLongNullable : Long? = null

    @MappableProperty
    var propFloat : Float = 0f

    @MappableProperty
    var propFloatNullable : Float? = null

    @MappableProperty
    var propDouble : Double = 0.0

    @MappableProperty
    var propDoubleNullable : Double? = null

    @MappableProperty
    var propStr : String = ""

    @MappableProperty
    var propStrNullable : String? = null

    @MappableProperty
    lateinit var propStrNullableLate : String

    @MappableProperty
    var propDate : Date = Date()

    @MappableProperty
    var propDateNullable : Date? = null

    @MappableProperty
    lateinit var propDateNullableLate : Date

    @MappableProperty
    var propEnum : TestEnum = TestEnum.value1

    @MappableProperty
    var propEnumNullable : TestEnum? = null

    @MappableProperty
    lateinit var propEnumNullableLate : TestEnum

    @MappableProperty
    @MappedBy(parserForward = TestEntityParser::class)
    var propCustom : TestEntity1 = TestEntity1()                // Need custom parser

    @MappableProperty
    @MappedBy(parserForward = TestEntityParserNullable::class)
    var propCustomNullable : TestEntity1? = null

    @MappableProperty
    @MappedBy(parserForward = TestEntityParser::class)
    lateinit var propCustomLate : TestEntity1

    @MappableProperty("propInt3")
    var propInt2 : Int = 0                          // Different name

    @MappableProperty("propIntNullable3")
    var propIntNullable2 : Int? = null

    @MappableProperty("propCustom3")
    @MappedBy(parserForward = TestEntityParser::class)
    var propCustom2 : TestEntity1 = TestEntity1()                // Need custom parser & different name

    @MappableProperty("propCustomNullable3")
    @MappedBy(parserForward = TestEntityParserNullable::class)
    var propCustomNullable2 : TestEntity1? = null

    @MappableProperty("propCustomLate3")
    @MappedBy(parserForward = TestEntityParser::class)
    lateinit var propCustomLate2 : TestEntity1

    @MappableProperty
    @MappedBy(parserForward = ChildListParserNullable::class)
    var innerCollection : MutableList<ChildDb>? = null                 // custom parser

    @MappableProperty
    @MappedBy(parserForward = ChildListParser::class)
    lateinit var innerCollectionLate : MutableList<ChildDb>

    @MappableProperty
    var child : ChildDb = ChildDb()                 // Property has mapper

    @MappableProperty
    var childNullable : ChildDb? = null

    @MappableProperty
    lateinit var childLate : ChildDb

    var propIntSkipped : Int = 0                // not mapped
}