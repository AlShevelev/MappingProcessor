package com.syleiman.mapperTests

import com.syleiman.mapperTests.db.ParentDb
import com.syleiman.mapperTests.db.ParentDbMapper
import org.junit.Test

/** */
class MappingUnitTests
{
    @Suppress("UNUSED_VARIABLE")
    @Test(expected = kotlin.UninitializedPropertyAccessException::class)
    fun howToUse() {
        // ParentDb lateinit fields are not initialized so kotlin.UninitializedPropertyAccessException must be here
        val dto = ParentDbMapper().map(ParentDb())
    }
}