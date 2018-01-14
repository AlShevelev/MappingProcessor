package com.syleiman.mapperTests.db

import com.syleiman.mapperTests.customParsers.ChildOfChildListParser
import com.syleiman.mappingProcessor.annotations.MappableEntity
import com.syleiman.mapperTests.dto.ChildDto
import com.syleiman.mappingProcessor.annotations.MappableProperty
import com.syleiman.mappingProcessor.annotations.MappedBy

/** */
@MappableEntity(mapTo = ChildDto::class, isTwoWay = false)
class ChildDb
{
    @MappableProperty
    var propInt : Int = 0

    @MappableProperty
    @MappedBy(parserForward = ChildOfChildListParser::class)
    lateinit var innerCollectionLate : MutableList<ChildOfChildDb>
}