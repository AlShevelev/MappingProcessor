package com.syleiman.mapperTests.dto

import com.syleiman.mapperTests.customClasses.TestEntity2
import com.syleiman.mapperTests.customClasses.TestEnum
import java.util.*

/** */
class ParentDto
{
    var propShort : Short = 0
    var propShortNullable : Short? = null

    var propInt : Int = 0
    var propIntNullable : Int? = null

    var propLong : Long = 0
    var propLongNullable : Long? = null

    var propFloat : Float = 0f
    var propFloatNullable : Float? = null

    var propDouble : Double = 0.0
    var propDoubleNullable : Double? = null

    var propStr : String = ""
    var propStrNullable : String? = null
    lateinit var propStrNullableLate : String

    var propDate : Date = Date()
    var propDateNullable : Date? = null
    lateinit var propDateNullableLate : Date

    var propEnum : TestEnum = TestEnum.value1
    var propEnumNullable : TestEnum? = null
    lateinit var propEnumNullableLate : TestEnum

    var propCustom : TestEntity2 = TestEntity2()                // Need custom parser
    var propCustomNullable : TestEntity2? = null
    lateinit var propCustomLate : TestEntity2

    var propInt3 : Int = 0                          // Different name
    var propIntNullable3 : Int? = null

    var propCustom3 : TestEntity2 = TestEntity2()                // Need custom parser & different name
    var propCustomNullable3 : TestEntity2? = null
    lateinit var propCustomLate3 : TestEntity2

    var innerCollection : MutableList<ChildDto>? = null
    lateinit var innerCollectionLate : MutableList<ChildDto>

    var child : ChildDto = ChildDto()
    var childNullable : ChildDto? = null
    lateinit var childLate : ChildDto
}