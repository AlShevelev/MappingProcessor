package com.syleiman.mapperTests.db

import com.syleiman.mappingProcessor.annotations.MappableEntity
import com.syleiman.mapperTests.dto.ChildOfChildDto
import com.syleiman.mappingProcessor.annotations.MappableProperty

/** */
@MappableEntity(mapTo = ChildOfChildDto::class, isTwoWay = false)
class ChildOfChildDb
{
    @MappableProperty
    var propInt : Int = 0
}