package io.github.cgglyle.scan

import io.github.cgglyle.bean.BeanContainer
import io.github.cgglyle.scan.test.TestA
import io.github.cgglyle.scan.test.dir.TestC
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ComponentScanProcessorTest {

    @Test
    fun testClassPathScan() {
        //given
        val componentScanProcessor = ComponentScanProcessor()

        //when
        val scan = componentScanProcessor.scan("io.github.cgglyle.scan.test")

        //then
        Assertions.assertEquals(scan.size, 2)
        Assertions.assertTrue(scan.any { it.type == TestA::class.java })
        Assertions.assertTrue(scan.any { it.type == TestC::class.java })
    }

    private lateinit var beanContainer: BeanContainer
    @BeforeEach
    fun setUp() {
        //given
        val componentScanProcessor = ComponentScanProcessor()
        val beanDefinitions = componentScanProcessor.scan("io.github.cgglyle.scan.test")
        val beanContainer = BeanContainer()
        val annotationBeanNameGenerator = AnnotationBeanNameGenerator()
        val pairs = beanDefinitions.map { Pair(annotationBeanNameGenerator.generatorBeanName(it), it) }
        pairs.forEach { (beanName, beanDefinition) ->
            beanContainer.saveBeanDefinition(beanName, beanDefinition)
        }
        this.beanContainer = beanContainer
    }

    @Test
    fun testCLassPathGetBean() {
        // when
        val beanA = beanContainer.getBean("testA")
        val beanC = beanContainer.getBean("testC")

        // then
        Assertions.assertNotNull(beanA)
        Assertions.assertNotNull(beanC)
    }

    @Test
    fun testAutowired() {
        // when
        val beanA = beanContainer.getBean("testA") as TestA
        val beanC = beanContainer.getBean("testC") as TestC

        // then
        Assertions.assertNotNull(beanA)
        Assertions.assertNotNull(beanC)
        Assertions.assertEquals(beanA, beanC.testA)
        Assertions.assertEquals(false, beanC.testCIsInitialized())
        Assertions.assertEquals(beanA, beanA.testA)
    }

    @Test
    fun testScope() {
        // when
        val prototypeTest = beanContainer.getBean("prototypeTest")
        val prototypeTestB = beanContainer.getBean("prototypeTest")

        // then
        Assertions.assertNotEquals(prototypeTest, prototypeTestB)
    }
}