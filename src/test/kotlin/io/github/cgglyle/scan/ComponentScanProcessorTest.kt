package io.github.cgglyle.scan

import io.github.cgglyle.bean.BeanContainer
import io.github.cgglyle.scan.test.TestA
import io.github.cgglyle.scan.test.dir.TestC
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
        Assertions.assertEquals(scan.size, 2)
        Assertions.assertTrue(scan.any { it.type == TestA::class.java })
        Assertions.assertTrue(scan.any { it.type == TestC::class.java })
    }

    @Test
    fun testCLassPathGetBean() {
        //given
        val componentScanProcessor = ComponentScanProcessor()
        val beanDefinitions = componentScanProcessor.scan("io.github.cgglyle.scan.test")
        val beanContainer = BeanContainer()
        val annotationBeanNameGenerator = AnnotationBeanNameGenerator()
        val pairs = beanDefinitions.map { Pair(annotationBeanNameGenerator.generatorBeanName(it), it) }
        pairs.forEach { (beanName, beanDefinition) ->
            beanContainer.saveBeanDefinition(beanName, beanDefinition)
        }

        // when
        val beanA = beanContainer.getBean("testA")
        val beanC = beanContainer.getBean("testC")

        // then
        Assertions.assertNotNull(beanA)
        Assertions.assertNotNull(beanC)
    }
}