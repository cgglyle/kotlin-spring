package io.github.cgglyle.name

import io.github.cgglyle.AnnotationBeanNameGenerator
import io.github.cgglyle.BeanDefinition
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AnnotationBeanNameGeneratorTest {
    private lateinit var beanNameGenerator: AnnotationBeanNameGenerator

    @BeforeEach
    fun setUp() {
        beanNameGenerator = AnnotationBeanNameGenerator()
    }

    @Test
    fun generatorBeanName() {
        // given
        val beanDefinition = BeanDefinition(TestA::class.java)
        val DNSBeanDefinition = BeanDefinition(DNSTestA::class.java)

        // when
        val beanName = beanNameGenerator.generatorBeanName(beanDefinition)
        val dnsBeanName = beanNameGenerator.generatorBeanName(DNSBeanDefinition)

        // then
        assertEquals("testA", beanName)
        assertEquals("DNSTestA", dnsBeanName)
    }
}