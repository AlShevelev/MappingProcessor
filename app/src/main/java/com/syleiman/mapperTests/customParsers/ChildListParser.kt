package com.syleiman.mapperTests.customParsers

import com.syleiman.mapperTests.db.ChildDb
import com.syleiman.mapperTests.dto.ChildDto
import com.syleiman.mappingProcessor.customParser.ParserInterface

/** */
class ChildListParser : ParserInterface<MutableList<ChildDb>, MutableList<ChildDto>>
{
    /** */
    override fun parse(source: MutableList<ChildDb>): MutableList<ChildDto> = source.map { ChildDto() }.toMutableList()
}

class ChildListParserNullable : ParserInterface<MutableList<ChildDb>?, MutableList<ChildDto>?>
{
    private val childListParser = ChildListParser()

    /** */
    override fun parse(source: MutableList<ChildDb>?): MutableList<ChildDto>? = source?.let { childListParser.parse(it) }
}