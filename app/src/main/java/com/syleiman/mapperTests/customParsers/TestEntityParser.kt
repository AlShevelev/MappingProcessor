package com.syleiman.mapperTests.customParsers

import com.syleiman.mappingProcessor.customParser.ParserInterface
import com.syleiman.mapperTests.customClasses.TestEntity1
import com.syleiman.mapperTests.customClasses.TestEntity2

/** */
class TestEntityParser : ParserInterface<TestEntity1, TestEntity2>
{
    /** */
    override fun parse(source: TestEntity1): TestEntity2 = TestEntity2().apply { propInt = source.propInt  }
}

/** */
class TestEntityParserNullable : ParserInterface<TestEntity1?, TestEntity2?>
{
    private val testEntityParser = TestEntityParser()

    /** */
    override fun parse(source: TestEntity1?): TestEntity2? = source?.let {testEntityParser.parse(it)}
}