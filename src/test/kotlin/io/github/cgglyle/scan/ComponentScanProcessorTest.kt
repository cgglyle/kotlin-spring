package io.github.cgglyle.scan

import io.github.cgglyle.ComponentScanProcessor
import io.github.cgglyle.scan.test.TestA
import io.github.cgglyle.scan.test.TestB
import io.github.cgglyle.scan.test.dir.TestC
import io.github.cgglyle.scan.test.dir.TestD
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ComponentScanProcessorTest {

    @Test
    fun testClassPathScan() {
        //given
        val componentScanProcessor = ComponentScanProcessor()

        //when
        val scan = componentScanProcessor.scan("io.github.cgglyle.scan.test")

        //then
        Assertions.assertEquals(scan.size, 4)
        Assertions.assertTrue(scan.any { it == TestA::class.java })
        Assertions.assertTrue(scan.any { it == TestB::class.java })
        Assertions.assertTrue(scan.any { it == TestC::class.java })
        Assertions.assertTrue(scan.any { it == TestD::class.java })
    }
}