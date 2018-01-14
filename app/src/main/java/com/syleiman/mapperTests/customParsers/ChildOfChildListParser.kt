package com.syleiman.mapperTests.customParsers

import com.syleiman.mapperTests.db.ChildOfChildDb
import com.syleiman.mapperTests.dto.ChildOfChildDto
import com.syleiman.mappingProcessor.customParser.ParserInterface

/** */
class ChildOfChildListParser: ParserInterface<MutableList<ChildOfChildDb>, MutableList<ChildOfChildDto>>
{
    /** */
    override fun parse(source: MutableList<ChildOfChildDb>): MutableList<ChildOfChildDto> = source.map { ChildOfChildDto() }.toMutableList()
}
